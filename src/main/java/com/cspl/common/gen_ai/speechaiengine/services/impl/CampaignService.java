package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.dto.CampaignMetadata;
import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.cspl.common.gen_ai.speechaiengine.dtos.request.CampaignRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.CampaignResponseDto;
import com.cspl.common.gen_ai.speechaiengine.exceptions.RestServiceException;
import com.cspl.common.gen_ai.speechaiengine.mappers.CampaignMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecordLeadMapping;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.ErrorCodes;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.CampaignRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.ICampaignService;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordService;
import com.cspl.common.gen_ai.speechaiengine.services.IRuleService;
import com.cspl.common.gen_ai.speechaiengine.utils.IRedisServiceManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.Math.min;

/**
 * @author vineet.rajput
 * @class CampaignService
 * @use: This class is used to perform CRUD operations on Campaign
 * Service class for Campaign
 */
@Slf4j
@Service("campaignService")
@AllArgsConstructor
public class CampaignService implements ICampaignService {

    /**
     * The CampaignRepository
     */
    private final CampaignRepository campaignRepository;

    /**
     * The CampaignMapper
     */
    private final CampaignMapper campaignMapper;

    /**
     * The EventRecordRepository
     */
    private final IEventRecordService eventRecordService;

    /**
     * The EventRecordRepository
     */
    private final EventRecordRepository eventRecordRepository;

    /**
     * The Redis Service Manager
     */
    private final IRedisServiceManager redisServiceManager;

    /**
     * rule service to get rules
     */
    private final IRuleService ruleService;

    /**
     * Get Campaign by campaignId
     * @param campaignId
     * @param headers
     * @return CampaignResponseDto
     */
    @Override
    public CampaignResponseDto getCampaign(String campaignId, boolean metadata, Map<String, String> headers) throws Exception {
        Campaign campaign = getCampaign(campaignId);
        if(metadata) {
            populateCampaignMetadata(campaign);
        }
        return campaignMapper.entityToResponseDto(campaign);
    }

    /**
     * Get all Campaigns
     *
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @param headers
     * @return List<CampaignResponseDto>
     */
    @Override
    public Page<CampaignResponseDto> getAllCampaigns(int page, int size, String sortBy, String sortDir, boolean metadata, Map<String, String> headers) {
        Pageable pageable = PageRequest.of(page, size, sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<Campaign> campaignList = campaignRepository.findAll(pageable);
        if(metadata) {
            campaignList.forEach(this::populateCampaignMetadata);
        }
        return campaignList.map(campaignMapper::entityToResponseDto);
    }

    /**
     * Update Campaign by campaignId
     * @param campaignId
     * @param campaignRequestDto
     * @param headers
     * @return CampaignResponseDto
     */
    @Override
    public CampaignResponseDto updateCampaign(String campaignId, CampaignRequestDto campaignRequestDto, Map<String, String> headers, boolean completed) throws Exception {

        Campaign savedCampaign = getCampaign(campaignId);
        Campaign updatedCampaign = campaignMapper.partialUpdate(campaignRequestDto, savedCampaign);
        if (completed && updatedCampaign.getCampaignStatus().equals(CampaignStatus.PARTIALLY_CREATED)){
            if (isValidCampaign(updatedCampaign)) {
                updatedCampaign.setCampaignStatus(CampaignStatus.CREATED);
            }
            else{
                throw new RestServiceException("Cannot create campaign pls provide all required fields");
            }
        }
        campaignRepository.save(updatedCampaign);
        return campaignMapper.entityToResponseDto(updatedCampaign);
    }

    /**
     * Create Campaign
     * @param campaignRequestDto
     * @param headers
     * @return CampaignResponseDto
     */
    @Override
    public CampaignResponseDto createCampaign(CampaignRequestDto campaignRequestDto, Map<String, String> headers) {
        Campaign campaign = campaignMapper.toEntity(campaignRequestDto);
        campaign.setCampaignStatus(CampaignStatus.PARTIALLY_CREATED);
        Campaign savedCampaign = campaignRepository.save(campaign);
        return campaignMapper.entityToResponseDto(savedCampaign);
    }

    /**
     * Delete Campaign by campaignId
     * @param campaignId
     * @param headers
     * @return ResponseEntity<String>
     */
    @Override
    public ResponseEntity<String> deleteCampaign(String campaignId, Map<String, String> headers) throws Exception {
        Campaign campaign = getCampaign(campaignId);
        campaign.setDeleted(true);
        return ResponseEntity.accepted().body("{\"message\": \"Campaign deleted successfully\"}");
    }


    /**
     * Convert List<Campaign> to List<CampaignResponseDto>
     * @param campaignList
     * @return List<CampaignResponseDto>
     */
    private List<CampaignResponseDto> entityListToResponseDtoList(List<Campaign> campaignList) {
        List<CampaignResponseDto> responseDtoList = new ArrayList<>();
        for(Campaign campaign : campaignList) {
            responseDtoList.add(campaignMapper.entityToResponseDto(campaign));
        }
        return responseDtoList;
    }

    /**
     *
     * @param campaignId
     * @return
     * @throws Exception
     */
    @Override
    public Campaign getCampaign(String campaignId) throws Exception {
        return campaignRepository.findById(campaignId).orElseThrow(() -> new RestServiceException(ErrorCodes.CAMPAIGN_NOT_FOUND.getCode()));
    }

    @Override
    public CampaignResponseDto updateCampaignStatus(String campaignId, CampaignStatus status, Map<String, String> headers) throws Exception {
        Campaign campaign = getCampaign(campaignId);
        if(campaign.getCampaignStatus().equals(CampaignStatus.PARTIALLY_CREATED)){
            throw new RestServiceException(ErrorCodes.CAMPAIGN_STATUS_PARTIALLY_CREATED.getCode());
        }
        campaign.setCampaignStatus(status);
        if(status.equals(CampaignStatus.ACTIVE)){
            processCampaign(campaign);
        }
        for(EventRecordLeadMapping eventRecordLeadMapping : campaign.getEventRecordLeadMappings()){
            redisServiceManager.set(eventRecordLeadMapping.getEventRecord().getId(),status);
        }
        return campaignMapper.entityToResponseDto(campaignRepository.save(campaign));
    }


    public void processCampaign(Campaign campaign){
        ZonedDateTime nowInZone = ZonedDateTime.now(campaign.getZoneId());
        LocalDate today = nowInZone.toLocalDate();
        LocalDate startDate = LocalDate.parse(campaign.getStartDate());
        LocalDate endDate = LocalDate.parse(campaign.getEndDate());

        if((startDate.isBefore(today)||startDate.isEqual(today)) && (endDate.isAfter(today)||(endDate.isEqual(today)))){
            List<EventRecord> eventRecords = populateEventRecordsStatusAsIdle(campaign);
            eventRecordRepository.saveAll(eventRecords);
        }
    }

    private List<EventRecord> populateEventRecordsStatusAsIdle(Campaign campaign) {
        List<EventRecordLeadMapping> eventRecordLeadMappings = campaign.getEventRecordLeadMappings();
        List<EventRecord> eventRecords = new ArrayList<>();
        if(Objects.nonNull(eventRecordLeadMappings) && !eventRecordLeadMappings.isEmpty()) {
            eventRecordLeadMappings.forEach(eventRecordLeadMapping -> {
                EventRecord eventRecord = eventRecordLeadMapping.getEventRecord();
                if (eventRecord.getEventStatus().equals(EventStatus.CREATED) || eventRecord.getEventStatus().equals(EventStatus.PAUSED)) {
                    eventRecord.setEventStatus(EventStatus.IDLE);
                    eventRecords.add(eventRecord);
                }
            });
        }
        return eventRecords;
    }

    private List<EventRecord> populateEventRecordsStatus(Campaign campaign, Set<EventStatus> fromStatus , EventStatus toStatus) {
        List<EventRecordLeadMapping> eventRecordLeadMappings = campaign.getEventRecordLeadMappings();
        List<EventRecord> eventRecords = new ArrayList<>();
        if(Objects.nonNull(eventRecordLeadMappings) && !eventRecordLeadMappings.isEmpty()) {
            eventRecordLeadMappings.forEach(eventRecordLeadMapping -> {
                EventRecord eventRecord = eventRecordLeadMapping.getEventRecord();
                if (fromStatus.contains(eventRecord.getEventStatus())) {
                    eventRecord.setEventStatus(toStatus);
                }
                eventRecords.add(eventRecord);
            });
        }
        return eventRecords;
    }

    private void populateCampaignMetadata(Campaign campaign) {
        if (campaign == null || campaign.getEventRecordLeadMappings() == null) return;

        var ref = new Object() {
            Long totalLeads = 0L;
            Long completedLeads = 0L;
            Long failedLeads = 0L;
            Long pendingLeads = 0L;
            Long inProgressLeads = 0L;
            Long pickedLeads = 0L;
            Double totalDurationInSeconds = 0D;
            Double avgDurationInSeconds = 0D;
            Double pickUpRate = 0D;
        };

        List<EventRecordLeadMapping> eventRecordLeadMappings = campaign.getEventRecordLeadMappings();
        List<EventRecord> eventRecords = eventRecordLeadMappings.stream()
                .map(EventRecordLeadMapping::getEventRecord)
                .filter(Objects::nonNull)
                .toList();

        for (EventRecord eventRecord : eventRecords) {
            if (eventRecord.getEventStatus() == null) continue;
            switch (eventRecord.getEventStatus()) {
                case INITIATED -> ref.inProgressLeads++;
                case COMPLETED -> ref.completedLeads++;
                case FAILED -> ref.failedLeads++;
                case PENDING -> ref.pendingLeads++;
                default -> {
                    // Do nothing
                }
            }
        }

        ref.totalLeads = (long) eventRecords.size();

        ref.totalDurationInSeconds = eventRecords.stream()
                .map(EventRecord::getMetaData)
                .filter(Objects::nonNull)
                .flatMap(metadata -> {
                    List<CallRecordLog> logs = metadata.getCallRecordLogList();
                    return logs != null ? logs.stream() : Stream.empty();
                })
                .filter(Objects::nonNull)
                .filter(callRecordLog -> CallStatus.COMPLETED.equals(callRecordLog.getCallStatus()))
                .map(CallRecordLog::getCallDetailsDto)
                .filter(Objects::nonNull)
                .mapToDouble(details -> {
                    Object duration = details.getDuration();
                    try {
                        return duration != null ? Double.parseDouble(duration.toString()) : 0;
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .sum();

        ref.pickedLeads = eventRecords.stream()
                .map(EventRecord::getMetaData)
                .filter(Objects::nonNull)
                .flatMap(metadata -> {
                    List<CallRecordLog> logs = metadata.getCallRecordLogList();
                    return logs != null ? logs.stream() : Stream.empty();
                })
                .filter(Objects::nonNull)
                .filter(callRecordLog -> CallStatus.COMPLETED.equals(callRecordLog.getCallStatus()))
                .map(CallRecordLog::getCallDetailsDto)
                .filter(Objects::nonNull)
                .filter(details -> {
                    Object duration = details.getDuration();
                    try {
                        return duration != null && Double.parseDouble(duration.toString()) > 15;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .count();

        ref.avgDurationInSeconds = ref.completedLeads > 0
                ? ref.totalDurationInSeconds / ref.completedLeads
                : 0D;

        ref.pickUpRate = ref.totalLeads > 0
                ? ((double) ref.pickedLeads / ref.totalLeads) * 100
                : 0D;

        campaign.setMetadata(CampaignMetadata.builder()
                .totalLeads(ref.totalLeads)
                .pickedLeads(ref.pickedLeads)
                .completedLeads(ref.completedLeads)
                .failedLeads(ref.failedLeads)
                .pendingLeads(ref.pendingLeads)
                .inProgressLeads(ref.inProgressLeads)
                .totalDurationInSeconds(ref.totalDurationInSeconds)
                .avgDurationInSeconds(ref.avgDurationInSeconds)
                .build());
    }

    public boolean isValidCampaign(Campaign campaign){
        return Objects.nonNull(campaign.getStartDate()) &&
                Objects.nonNull(campaign.getEndDate()) &&
                Objects.nonNull(campaign.getAgentId()) &&
                Objects.nonNull(campaign.getDialer()) &&
                Objects.nonNull(campaign.getFromPhoneNumber()) &&
                !campaign.getFromPhoneNumber().isEmpty() &&
                Objects.nonNull(campaign.getZoneId()) &&
                Objects.nonNull(campaign.getDailyStartTime()) &&
                Objects.nonNull(campaign.getDailyStopTime());
    }

    public List<RuleCreationDTO> getAllRulesByCampaign(String campaignId, int page, int size, String sortBy, String sortDir,Map<String,String> headers) throws Exception {
        Campaign campaign = getCampaign(campaignId);
        List<String> campaignRuleIds = campaign.getRuleId();
        List<String> paginatedRuleIds = new ArrayList<>();
        List<RuleCreationDTO> ruleCreationDTOList;
        for(int i=page*size;i<min((page+1)*size,campaignRuleIds.size());i++){
            paginatedRuleIds.add(campaignRuleIds.get(i));
        }
        ruleCreationDTOList = ruleService.getAllRuleByIds(paginatedRuleIds);
        return ruleCreationDTOList;
    }
}
