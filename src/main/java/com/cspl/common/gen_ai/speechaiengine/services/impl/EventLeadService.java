package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.controller.v1.EventLeadRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO;
import com.cspl.common.gen_ai.speechaiengine.exceptions.RestServiceException;
import com.cspl.common.gen_ai.speechaiengine.mappers.EventLeadMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.*;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.*;
import com.cspl.common.gen_ai.speechaiengine.services.ICampaignService;
import com.cspl.common.gen_ai.speechaiengine.services.IEventLeadService;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordService;
import com.cspl.common.gen_ai.speechaiengine.services.IFileCloudTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service("eventLeadService")
@AllArgsConstructor
public class EventLeadService implements IEventLeadService {

    /**
     * The Event record repository.
     */
    private final EventRecordRepository eventRecordRepository;

    /**
     * The Event lead mapper.
     */
    private final EventLeadMapper eventLeadMapper;

    /**
     * The Object mapper.
     */
    private final ObjectMapper objectMapper;

    /**
     * The Event lead repository.
     */
    private final EventLeadRepository eventLeadRepository;

    /**
     * The Event record service.
     */
    private final IEventRecordService eventRecordService;

    /**
     * The Campaign service.
     */
    private final ICampaignService campaignService;

    /**
     * The Campaign repository.
     */
    private final EventRecordLeadRepository eventRecordLeadRepository;

    /**
     * The Campaign repository.
     */
    private final CampaignRepository campaignRepository;

    private final EventRecordLeadMappingRepository eventRecordLeadMappingRepository;

    private final IFileCloudTransferService fileCloudTransferService;

    /**
     * Process csv file as string.
     * @param filePartMono
     * @return
     */
    @Override
    public Mono<String> processCsvFileAsString(String campaignId, Mono<FilePart> filePartMono) {
        Mono<String> csvJsonMono = filePartMono
                .flatMap(filePart -> {
                    // Extract all content to a single byte array
                    return filePart.content()
                            .map(dataBuffer -> {
                                byte[] bytes = new byte[dataBuffer.readableByteCount()];
                                dataBuffer.read(bytes);
                                return bytes;
                            })
                            .reduce(new byte[0], (allBytes, bytes) -> {
                                byte[] result = new byte[allBytes.length + bytes.length];
                                System.arraycopy(allBytes, 0, result, 0, allBytes.length);
                                System.arraycopy(bytes, 0, result, allBytes.length, bytes.length);
                                return result;
                            });
                })
                .flatMap(bytes -> Mono.fromCallable(() -> {
                    String content = new String(bytes, StandardCharsets.UTF_8);
                    CSVParser csvParser = CSVFormat.DEFAULT
                            .withFirstRecordAsHeader()
                            .withIgnoreHeaderCase()
                            .withTrim()
                            .parse(new StringReader(content));

                    // Convert to list of maps
                    List<Map<String, String>> rows = new ArrayList<>();
                    for (CSVRecord record : csvParser) {
                        Map<String, String> row = new HashMap<>();
                        csvParser.getHeaderNames().forEach(header ->
                                row.put(header, record.get(header))
                        );
                        rows.add(row);
                    }

                    // Convert to JSON
                    return objectMapper.writeValueAsString(rows);
                }).subscribeOn(Schedulers.boundedElastic()));

        return csvJsonMono.doOnSuccess(csvJson -> {
            try {
                List<Map<String,Object>> eventLeadRequestData = objectMapper.readValue(csvJson, objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));
                List<EventLeadRequestDto> eventLeadRequestDtoList = new ArrayList<>();
                eventLeadRequestData.forEach(eventLeadRequest ->{
                    EventLeadRequestDto eventLeadRequestDto = EventLeadRequestDto.builder().requestData(eventLeadRequest).build();
                    eventLeadRequestDtoList.add(eventLeadRequestDto);
                });
                uploadEventLeads(campaignId, eventLeadRequestDtoList);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Upload event leads or create new for a campaign
     * @param campaignId
     * @param eventLeadRequestDtoList
     * @throws Exception
     */
    @Override
    public void uploadEventLeads(String campaignId, @NotNull List<EventLeadRequestDto> eventLeadRequestDtoList) throws Exception {
        Campaign campaign = campaignService.getCampaign(campaignId);
        List<EventLead> eventLeads = new ArrayList<>();
        for (EventLeadRequestDto eventLeadRequestDto : eventLeadRequestDtoList) {
            EventLead eventLead = eventLeadMapper.toEntity(eventLeadRequestDto);
            if(!eventLead.getRequestData().containsKey("phone")) throw new RestServiceException("No \"phone\" field provided for eventLead");
            eventLeads.add(eventLead);
        }
        eventLeads = eventLeadRepository.saveAll(eventLeads);
        List<EventRecordLeadMapping> eventRecordLeadMappings = new ArrayList<>();
        List<EventRecord> eventRecords = new ArrayList<>();
        for(EventLead eventLead : eventLeads) {
            EventRecordLeadMapping eventRecordLeadMapping = EventRecordLeadMapping.builder()
                    .eventRecord(new EventRecord())
                    .eventLead(eventLead)
                    .campaignId(campaignId)
                    .build();
            eventRecords.add(eventRecordLeadMapping.getEventRecord());
            eventRecordLeadMappings.add(eventRecordLeadMapping);
        }
        eventRecords = eventRecordRepository.saveAll(eventRecords);
        eventRecordLeadMappings.forEach(eventRecordLeadMapping -> eventRecordService.populateEventRecord(eventRecordLeadMapping.getEventRecord(), eventRecordLeadMapping.getEventLead(), campaign));
        eventRecordRepository.saveAll(eventRecords);
        eventRecordLeadMappings = eventRecordLeadRepository.saveAll(eventRecordLeadMappings);
        if(Objects.nonNull(campaign.getEventRecordLeadMappings())){
            campaign.getEventRecordLeadMappings().addAll(eventRecordLeadMappings);
        }
        else{
            campaign.setEventRecordLeadMappings(eventRecordLeadMappings);
        }
        campaignRepository.save(campaign);
    }

    /**
     * to get leads of campaign paginated
     *
     * @param campaignId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     * @throws Exception
     */
    @Override
    public Page<EventLeadResponseDTO> getAllEventLeads(String campaignId, int page, int size, String sortBy, String sortDir) throws Exception {
        Pageable pageable = PageRequest.of(page, size, sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<EventRecordLeadMapping> eventRecordLeadMappings =  eventRecordLeadMappingRepository.findByCampaignId(campaignId,pageable);
        return eventRecordLeadMappings.map(EventRecordLeadMapping::getEventRecord).map(eventRecord ->{
            EventLeadResponseDTO eventLeadResponseDTO = EventLeadResponseDTO.builder()
                    .id(eventRecord.getId())
                    .eventStatus(eventRecord.getEventStatus())
//                    .recordingUrl(eventRecord.getMetaData() != null ? (eventRecord.getMetaData().getCallRecordLogList().stream().filter(callRecordLog -> callRecordLog.getCallStatus().equals(CallStatus.COMPLETED)).findFirst().orElse(null)!=null ?
//                                eventRecord.getMetaData().getCallRecordLogList().stream().filter(callRecordLog -> callRecordLog.getCallStatus().equals(CallStatus.COMPLETED)).findFirst().get().getRecordingUrl() : null) : null)
                    .requestData(eventRecord.getRequestData())
                    .build();
            if(Objects.nonNull(eventRecord.getMetaData()) && Objects.nonNull(eventRecord.getMetaData().getCallRecordLogList())){
                eventLeadResponseDTO.setRecordingUrl(eventRecord.getMetaData().getCallRecordLogList().stream()
                        .filter(callRecordLog -> callRecordLog.getCallStatus().equals(CallStatus.COMPLETED))
                        .findFirst()
                        .map(callRecordLog -> fileCloudTransferService.getSignedUrl(callRecordLog.getCallSid()+".mp3",24))
                        .orElse(null));
            }
            return eventLeadResponseDTO;
        });
    }
}
