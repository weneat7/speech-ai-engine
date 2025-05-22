package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.dto.EventRecordMetadata;
import com.cspl.common.gen_ai.speechaiengine.events.application.PullEvent;
import com.cspl.common.gen_ai.speechaiengine.mappers.CallRecordLogMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;
import com.cspl.common.gen_ai.speechaiengine.services.selectors.IAfterCallServiceFactory;
import com.cspl.common.gen_ai.speechaiengine.utils.IRedisServiceManager;
import com.github.sonus21.rqueue.core.RqueueMessageEnqueuer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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


    @Override
    public void handleRecording(String recordingUrl ,String callSid, String recordingStatus, String encodedAuth){
        if(recordingStatus.equals("completed")){
            CallRecordLog callRecordLog = callRecordLogRepository.findByCallSid(callSid).orElseThrow(()->new RuntimeException("Call record not found with callSid: "+callSid));
            CallRecordingLogDTO callRecordingLogDTO = callRecordLogMapper.toCallRecordingDTO(callRecordLog);
            callRecordingLogDTO.setRecordingUrl(recordingUrl);
            callRecordingLogDTO.setEncodedAuth(encodedAuth);
            rqueueMessageEnqueuer.enqueue(AppConstants.CALL_RECORDING_QUEUE,callRecordingLogDTO);
        }
    }

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


    public void handleCallEndCompleted(CallRecordLog callRecordLog){
        if(callRecordLog.getCallStatus().equals(CallStatus.COMPLETED)){
            EventRecord eventRecord = eventRecordRepository.findById(callRecordLog.getEventKey()).orElseThrow(()->new RuntimeException("Event Record not found for call record for event id :"+callRecordLog.getEventKey()));
            eventRecord.setEventStatus(EventStatus.COMPLETED);
            afterCallServiceFactory.getAfterCallServiceByAiProvider(AIProvider.ELEVEN_LABS).ifPresent(afterCallService -> afterCallService.handleCallEndStatus(callRecordLog,callRecordLog.getCallStatus()));
            eventRecordRepository.save(eventRecord);
        }
    }
}
