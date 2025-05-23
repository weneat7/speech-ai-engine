package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.dtos.request.EventRecordRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventRecordResponseDto;
import com.cspl.common.gen_ai.speechaiengine.mappers.EventRecordMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.*;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.publishers.IPublisher;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordLeadMappingRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordService;
import com.cspl.common.gen_ai.speechaiengine.services.IFileCloudTransferService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * EventRecordService
 * @author : Vineet Rajput
 * This service class is responsible for handling event records.
 * It provides methods to create, update, retrieve, and delete event records,
 * as well as process event records and populate them from event leads.
 */
@Slf4j
@Service("eventRecordService")
@AllArgsConstructor
public class EventRecordService implements IEventRecordService {

    /**
     * The Event record repository.
     */
    private final EventRecordRepository eventRecordRepository;

    /**
     * The Event record mapper.
     */
    private final EventRecordMapper eventRecordMapper;

    /**
     * The Call event record publisher.
     */
    private final IPublisher callEventRecordPublisher;

    private final EventRecordLeadMappingRepository eventRecordLeadMappingRepository;

    private final IFileCloudTransferService fileCloudTransferService;

    /**
     * Create event record.
     *
     * @param eventRecordRequestDto the event record request dto
     * @param requestHeaders        the request headers
     * @return the event record response dto
     */
    @Override
    public EventRecordResponseDto createEventRecord(EventRecordRequestDto eventRecordRequestDto, Map<String,String> requestHeaders) {
        if(Objects.isNull(eventRecordRequestDto.getEventStatus())) {
            eventRecordRequestDto.setEventStatus(EventStatus.IDLE);
        }
        return eventRecordMapper.toResponseDto(eventRecordRepository.save(eventRecordMapper.toEntity(eventRecordRequestDto)));
    }

    /**
     * Update event record.
     *
     * @param eventId               the event id
     * @param eventRecordRequestDto  the event record request dto
     * @param requestHeaders         the request headers
     * @return the event record response dto
     */
    @Override
    public EventRecordResponseDto updateEventRecord(String eventId, EventRecordRequestDto eventRecordRequestDto, Map<String,String> requestHeaders) {
        EventRecord eventRecord = eventRecordRepository.findById(eventId).orElseThrow(()->new RuntimeException("No event record found for "+eventId));
        return eventRecordMapper.toResponseDto(eventRecordMapper.partialUpdate(eventRecordRequestDto,eventRecord));
    }

    /**
     * Get event record.
     *
     * @param eventId      the event id
     * @param requestHeaders the request headers
     * @return the event record response dto
     */
    @Override
    public EventRecordResponseDto getEventRecord(String eventId, Map<String,String> requestHeaders) {
        EventRecord eventRecord = eventRecordRepository.findById(eventId).orElseThrow(()->new RuntimeException("No event record found for "+eventId));
        EventRecordResponseDto eventRecordResponseDto =  eventRecordMapper.toResponseDto(eventRecord);

        eventRecordResponseDto.setRecordingUrl(eventRecord.getMetaData().getCallRecordLogList().stream()
                .filter(callRecordLog -> callRecordLog.getCallStatus().equals(CallStatus.COMPLETED))
                .findFirst()
                .map(callRecordLog -> fileCloudTransferService.getSignedUrl(callRecordLog.getCallSid()+".mp3",24))
                .orElse(null));

        return eventRecordResponseDto;
    }

    /**
     * Delete event record.
     *
     * @param eventId      the event id
     * @param requestHeaders the request headers
     * @return the string
     */
    @Override
    public String deleteEventRecord(String eventId, Map<String,String> requestHeaders) {
        eventRecordRepository.deleteById(eventId);
        return eventId;
    }

    /**
     * Process event record.
     *
     * @param eventRecord   the event record
     * @param requestHeaders the request headers
     */
    @Override
    public void processEventRecord(EventRecord eventRecord, Map<String,String> requestHeaders) throws JsonProcessingException {
        callEventRecordPublisher.publish(eventRecord);
    }

    /**
     * Populate event records.
     *
     * @param eventRecord   the event record
     * @param eventLead    the event lead
     * @param campaign      the campaign
     */
    @Override
    public EventRecord populateEventRecord(EventRecord eventRecord, EventLead eventLead, Campaign campaign ) {

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:00");

        LocalDate startDate = LocalDate.parse(campaign.getStartDate(), dateFormatter);
        LocalDate endDate = LocalDate.parse(campaign.getEndDate(), dateFormatter);
        LocalTime dailyStartTime = LocalTime.parse(campaign.getDailyStartTime(), timeFormatter);
        LocalTime dailyStopTime = LocalTime.parse(campaign.getDailyStopTime(), timeFormatter);

        LocalDateTime startDateTime = LocalDateTime.of(startDate, dailyStartTime);
        LocalDateTime endDateTime = LocalDateTime.of(endDate, dailyStopTime);

        ZonedDateTime startZonedDateTime = startDateTime.atZone(campaign.getZoneId());
        ZonedDateTime endZonedDateTime = endDateTime.atZone(campaign.getZoneId());

        ZonedDateTime startUtc = startZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);
        ZonedDateTime endUtc = endZonedDateTime.withZoneSameInstant(ZoneOffset.UTC);

        eventRecord.setStartDate(startUtc.toLocalDate().format(dateFormatter));
        eventRecord.setEndDate(endUtc.toLocalDate().format(dateFormatter));
        eventRecord.setDailyStartTime(startUtc.toLocalTime().format(timeFormatter));
        eventRecord.setDailyStopTime(endUtc.toLocalTime().format(timeFormatter));
        eventRecord.setStartDateTime(LocalDateTime.of(startUtc.toLocalDate(), startUtc.toLocalTime()));
        eventRecord.setEndDateTime(LocalDateTime.of(endUtc.toLocalDate(), endUtc.toLocalTime()));
        eventRecord.setFromPhoneNumber(campaign.getFromPhoneNumber().get(0));
        eventRecord.setToPhoneNumber(eventLead.getRequestData().get("phone").toString());
        eventRecord.setDialerType(campaign.getDialer());
        eventRecord.setEventStatus(EventStatus.CREATED);
        eventRecord.setMaxAttempts(campaign.getMaxAttempts());
        eventRecord.setBackOffTimeInMinutes(campaign.getBackOffTimeInMinutes());
        eventRecord.setStartDate(startDate.format(dateFormatter));
        eventRecord.setEndDate(endDate.format(dateFormatter));
        eventRecord.setAgentId(campaign.getAgentId());
        eventRecord.setRequestData(eventLead.getRequestData());

        return eventRecord;
    }

    /**
     * Validate and Process event records by sending them to eventRecord queue
     *
     * @param eventRecords  the event records
     * @param requestHeaders the request headers
     */
    @Override
    public void processEventRecords(List<EventRecord> eventRecords, Map<String,String> requestHeaders) throws JsonProcessingException {
        for(EventRecord eventRecord:eventRecords){
            callEventRecordPublisher.publish(eventRecord);
        }
    }

    @Override
    public List<EventRecord> populateEventLeadsToEventRecords(Campaign campaign) throws Exception {
        List<EventRecordLeadMapping> eventRecordLeadMappings = campaign.getEventRecordLeadMappings();
        eventRecordLeadMappings.forEach(eventRecordLeadMapping -> {
            EventRecord eventRecord = eventRecordLeadMapping.getEventRecord();
            EventLead eventLead = eventRecordLeadMapping.getEventLead();
            populateEventRecord(eventRecord, eventLead, campaign);
        });
        List<EventRecord> eventRecords = eventRecordLeadMappings.stream()
                .map(EventRecordLeadMapping::getEventRecord)
                .collect(Collectors.toList());
        return eventRecordRepository.saveAll(eventRecords);

    }

    /**
     * to update event status to new status by fetching event records currently in given zone
     * @param eventStatuses
     * @param eventStatus
     * @return
     */
    public List<EventRecord> findAndUpdateEventRecord(List<EventStatus> eventStatuses, EventStatus eventStatus, LocalDateTime localDateTime)
    {
        return eventRecordRepository.findAndUpdateEventRecordByEventStatusInAndDateBetweenStartDate_EndDateAndTimeBetweenDailyStartTime_DailyStopTime(eventStatuses,eventStatus,localDateTime);
    }

    /**
     * to update eventRecord or create new if not present
     * @param eventRecord
     * @return
     */
    public EventRecord createOrUpdateEventRecord(EventRecord eventRecord) {
        EventRecord savedEvent = null;
        if(Objects.nonNull(eventRecord.getId())){
            savedEvent = eventRecordRepository.findById(eventRecord.getId()).orElseThrow(()->new RuntimeException("Event Record Not Found for id "+eventRecord.getId()));
            savedEvent.setEventStatus(eventRecord.getEventStatus());
            savedEvent.setAgentId(eventRecord.getAgentId());
            savedEvent.setBackOffTimeInMinutes(eventRecord.getBackOffTimeInMinutes());
            savedEvent.setMetaData(eventRecord.getMetaData());
        }
        else {
            savedEvent=eventRecord;
        }
        return eventRecordRepository.save(savedEvent);
    }


    /**
     * to get event record of campaign paginated
     * @param campaignId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     * @throws Exception
     */
    @Override
    public List<EventRecord> getAllEventRecords(String campaignId, int page, int size, String sortBy, String sortDir) throws Exception {
        Pageable pageable = PageRequest.of(page, size, sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        List<EventRecordLeadMapping> eventRecordLeadMappings =  eventRecordLeadMappingRepository.findByCampaignId(campaignId,pageable).getContent();
        List<EventRecord> eventRecords = new ArrayList<>();
        for(EventRecordLeadMapping eventRecordLeadMapping : eventRecordLeadMappings){
            List<CallRecordLog> callRecordLogs = eventRecordLeadMapping.getEventRecord().getMetaData().getCallRecordLogList();
            for(CallRecordLog callRecordLog : callRecordLogs){
                callRecordLog.setRecordingUrl(fileCloudTransferService.getSignedUrl(callRecordLog.getCallSid()+".mp3",24));
            }
            eventRecords.add(eventRecordLeadMapping.getEventRecord());
        }
        return eventRecords;
    }
}
