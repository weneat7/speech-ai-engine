package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.dtos.request.EventRecordRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventRecordResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface IEventRecordService {

    /**
     * Creates a new event record.
     * @param eventRecordRequestDto
     * @param requestHeaders
     * @return
     */
    public EventRecordResponseDto createEventRecord(EventRecordRequestDto eventRecordRequestDto, Map<String,String> requestHeaders);

    /**
     * Updates an existing event record.
     * @param eventId
     * @param eventRecordRequestDto
     * @param requestHeaders
     * @return
     */
    public EventRecordResponseDto updateEventRecord(String eventId, EventRecordRequestDto eventRecordRequestDto, Map<String,String> requestHeaders);

    /**
     * Fetches an event record by its ID.
     * @param eventId
     * @param requestHeaders
     * @return
     */
    public EventRecordResponseDto getEventRecord(String eventId, Map<String,String> requestHeaders);

    /**
     * Fetches all event records with pagination.
     * @param eventId
     * @param requestHeaders
     * @return
     */
    public String deleteEventRecord(String eventId, Map<String,String> requestHeaders);

    /**
     * Processes an event record.
     * @param eventRecord
     * @param requestHeaders
     */
    public void processEventRecord(EventRecord eventRecord, Map<String,String> requestHeaders) throws JsonProcessingException;

    /**
     * Populates event records from a list of event leads.
     * @param eventRecord the event record
     * @param eventLead the event lead
     * @param campaign the campaign
     */
    public EventRecord populateEventRecord(EventRecord eventRecord, EventLead eventLead, Campaign campaign);

    /**
     * Processes a list of event records.
     * @param eventRecords
     * @param requestHeaders
     */
    public void processEventRecords(List<EventRecord> eventRecords, Map<String,String> requestHeaders) throws JsonProcessingException;

    /**
     * Fetches all event records with pagination.
     * @param campaign the campaign
     * @return
     */
    public List<EventRecord> populateEventLeadsToEventRecords(Campaign campaign) throws Exception;

    public List<EventRecord> findAndUpdateEventRecord(List<EventStatus> eventStatuses, EventStatus eventStatus, LocalDateTime localDateTime);

    public EventRecord createOrUpdateEventRecord(EventRecord eventRecord);

    public List<EventRecord> getAllEventLeads(String campaignId, int page, int size, String sortBy, String sortDir) throws Exception;

}
