package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.EventRecordMetadata;
import com.cspl.common.gen_ai.speechaiengine.events.application.PullEvent;
import com.cspl.common.gen_ai.speechaiengine.mappers.CallRecordLogMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecordLeadMapping;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordLeadMappingRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;
import com.cspl.common.gen_ai.speechaiengine.services.IRetryService;
import com.cspl.common.gen_ai.speechaiengine.services.selectors.IAfterCallServiceFactory;
import com.cspl.common.gen_ai.speechaiengine.utils.IRedisServiceManager;
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class AbstractCallingService implements ICallingService {

    @Autowired
    CallRecordLogRepository callRecordLogRepository;

    @Autowired
    RqueueMessageEnqueuer rqueueMessageEnqueuer;

    @Autowired
    CallRecordLogMapper callRecordLogMapper;

    @Autowired
    IAfterCallServiceFactory afterCallServiceFactory;

    @Autowired
    IRedisServiceManager redisServiceManager;

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Autowired
    EventRecordRepository eventRecordRepository;

    @Autowired
    EventRecordLeadMappingRepository eventRecordLeadMappingRepository;

    @Autowired
    IRetryService retryService;


    public void handleEventCallMapping(Map<String,Object> response, EventRecord eventRecord){
        String callSid = "";
        if(response.containsKey("request_uuid")) {
            callSid = response.get("request_uuid").toString();
        }
        else if(response.containsKey("sid")){
            callSid = response.get("sid").toString();
        }

        eventRecord.setEventStatus(EventStatus.INITIATED);

        if(Objects.isNull(eventRecord.getId())){
            eventRecord = eventRecordRepository.save(eventRecord);
        }

        CallRecordLog callRecordLog = callRecordLogRepository.save(CallRecordLog
                .builder()
                .from(eventRecord.getFromPhoneNumber())
                .phoneNumber(eventRecord.getToPhoneNumber())
                .callStatus(CallStatus.IN_PROGRESS)
                .callSid(callSid)
                .eventKey(eventRecord.getId())
                .build());

        EventRecordMetadata eventRecordMetadata;
        if(Objects.isNull(eventRecord.getMetaData())){
            eventRecordMetadata =
                    EventRecordMetadata.builder().callRecordLogList(new ArrayList<>()).build();
        }
        else {
            eventRecordMetadata =
                    eventRecord.getMetaData();
        }

        List<CallRecordLog> callRecordLogs = eventRecordMetadata.getCallRecordLogList();
        callRecordLogs.add(callRecordLog);

        eventRecordMetadata.setCallRecordLogList(callRecordLogs);
        eventRecord.setMetaData(eventRecordMetadata);

        eventRecordRepository.save(eventRecord);


        int callSize=0;
        if(Objects.nonNull(redisServiceManager.getValue(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE))){
            callSize = (Integer) redisServiceManager.getValue(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE);
        }
        redisServiceManager.set(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE, callSize +1);
    }

    public void pullCallAndClearRedis(String eventKey) {
        redisServiceManager.set(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE, (Integer) redisServiceManager.getValue(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE) - 1);
        eventPublisher.publishEvent(new PullEvent(this));
        if(redisServiceManager.isKeyPresent(eventKey)) redisServiceManager.delete(eventKey);
    }


    /**
     * Enhanced call end handling with retry logic integration
     */
    public void handleCallEndCompleted(CallRecordLog callRecordLog, String encodedAuth) {
        EventRecord eventRecord = eventRecordRepository.findById(callRecordLog.getEventKey())
                .orElseThrow(() -> new RuntimeException("Event Record not found for call record for event id :" + callRecordLog.getEventKey()));

        CallStatus callStatus = callRecordLog.getCallStatus();

        if (CallStatus.COMPLETED.equals(callStatus)) {
            // Call was successful - handle success
            handleCallSuccess(eventRecord, callRecordLog, encodedAuth);
        } else {
            // Call failed - process with retry logic
            handleCallFailureWithRetry(eventRecord, callRecordLog, callStatus);
        }

        eventRecordRepository.save(eventRecord);
    }

    /**
     * Handle successful call completion
     */
    private void handleCallSuccess(EventRecord eventRecord, CallRecordLog callRecordLog, String encodedAuth) {
        log.info("Call completed successfully for EventRecord: {}", eventRecord.getId());

        eventRecord.setEventStatus(EventStatus.COMPLETED);

        // Process with after-call service (existing logic)
        afterCallServiceFactory.getAfterCallServiceByAiProvider(AIProvider.ELEVEN_LABS)
                .ifPresent(afterCallService -> afterCallService.handleCallEndStatus(callRecordLog));
        CallRecordingLogDTO callRecordingLogDTO = callRecordLogMapper.toCallRecordingDTO(callRecordLog);
        callRecordingLogDTO.setEncodedAuth(encodedAuth);
        rqueueMessageEnqueuer.enqueue(AppConstants.CALL_RECORDING_QUEUE,callRecordingLogDTO);

    }

    /**
     * Handle call failure with retry logic
     */
    private void handleCallFailureWithRetry(EventRecord eventRecord, CallRecordLog callRecordLog, CallStatus callStatus) {
        log.info("Call failed for EventRecord: {} with status: {}", eventRecord.getId(), callStatus);

        // Find the campaign ID for this event record
        String campaignId = findCampaignIdForEventRecord(eventRecord);

        if (campaignId != null) {
            // Generate failure reason
            String failureReason = generateFailureReason(callStatus);

            // Let retry service handle the failure and determine retry logic
            retryService.processCallFailure(eventRecord.getId(), campaignId, callStatus, failureReason);

            log.info("Call failure processed by retry service for EventRecord: {}", eventRecord.getId());
        } else {
            // Fallback - mark as failed without retry
            eventRecord.setEventStatus(EventStatus.FAILED);
            log.warn("Could not find campaign for EventRecord: {}, marking as failed without retry", eventRecord.getId());
        }
    }

    /**
     * Find campaign ID for an event record through EventRecordLeadMapping
     */
    private String findCampaignIdForEventRecord(EventRecord eventRecord) {
        try {
            return eventRecordLeadMappingRepository.findByEventRecord_Id(eventRecord.getId())
                    .map(EventRecordLeadMapping::getCampaignId)
                    .orElse(null);
        } catch (Exception e) {
            log.error("Error finding campaign for EventRecord: {}", eventRecord.getId(), e);
            return null;
        }
    }

    /**
     * Generate human-readable failure reason based on call status
     */
    private String generateFailureReason(CallStatus callStatus) {
        return switch (callStatus) {
            case BUSY -> "Customer phone was busy";
            case NO_ANSWER -> "Customer did not answer the call";
            case FAILED -> "Technical failure during call setup or execution";
            default -> "Call failed with status: " + callStatus;
        };
    }

    /**
     * Check if an event record is a retry attempt
     */
    protected boolean isRetryAttempt(EventRecord eventRecord) {
        return eventRecord.getMetaData() != null &&
                eventRecord.getMetaData().getRetryMetadata() != null &&
                eventRecord.getMetaData().getRetryMetadata().getTotalAttempts() > 0;
    }

    /**
     * Log retry attempt information
     */
    protected void logRetryAttempt(EventRecord eventRecord) {
        if (isRetryAttempt(eventRecord)) {
            int attemptNumber = eventRecord.getMetaData().getRetryMetadata().getTotalAttempts();
            log.info("Initiating retry attempt #{} for EventRecord: {}", attemptNumber, eventRecord.getId());
        }
    }

}