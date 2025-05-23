package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.config.RestClientHelper;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.CallDetailsDto;
import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.dto.PlivoInitiateCallDTO;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@AllArgsConstructor
@Slf4j
public class PlivoCallingService extends AbstractCallingService {

    private final RestClientHelper restClientHelper;

    private final AppProperties.PlivoConfig plivoConfig;

    private final ObjectMapper objectMapper;

    @Override
    public void updateCallRecordStatus(String callSid, String status) throws Exception {
        CallRecordLog callRecordLog = callRecordLogRepository.findByCallSid(callSid).orElseThrow(()->new RuntimeException("No Call Record Found for callSid : "+callSid));
        if(redisServiceManager.isKeyPresent(callSid)){
            return;
        }
        callRecordLog.setCallStatus(CallStatus.getFromValue(status));
        pullCallAndClearRedis(callRecordLog.getEventKey());
        if (!callRecordLog.getCallStatus().equals(CallStatus.COMPLETED)) {
            callRecordLogRepository.save(callRecordLog);
        } else {
            Map<String, Object> callDetails = restClientHelper.call(plivoConfig.getCallUri() + callSid, Map.of(HttpHeaders.AUTHORIZATION, plivoConfig.getAuthToken()));
            callRecordLog.setCallDetailsDto(objectMapper.convertValue(callDetails, CallDetailsDto.class));
            handleCallEndCompleted(callRecordLog, plivoConfig.getAuthToken());
            redisServiceManager.set(callSid,status, 2, TimeUnit.MINUTES);
        }
    }

    @Override
    public void initiateCallFlowEvent(EventRecord eventRecord) throws Exception {
        PlivoInitiateCallDTO requestDto = PlivoInitiateCallDTO.builder()
                .from(eventRecord.getFromPhoneNumber())
                .to(eventRecord.getToPhoneNumber())
                .answerUrl(plivoConfig.getAnswerUrl())
                .answerMethod("GET")
                .hangupUrl(plivoConfig.getHangupUrl())
                .hangupMethod("GET")
                .build();

        Map<String,Object> response = restClientHelper.call(plivoConfig.getCallUri(), requestDto , Map.of(HttpHeaders.AUTHORIZATION,plivoConfig.getAuthToken()) , MediaType.APPLICATION_JSON);
        handleEventCallMapping(response,eventRecord);
    }

    @Override
    public String configureWebSocketConnection(String callSid) throws JsonProcessingException {

        Map<String,Object> recordCallDTO = new HashMap<>();
        recordCallDTO.put("time_limit",960);

        Map<String,Object> recordResponse = restClientHelper.call(plivoConfig.getCallUri() +callSid+"/Record/", recordCallDTO , Map.of(HttpHeaders.AUTHORIZATION, plivoConfig.getAuthToken()) , MediaType.APPLICATION_JSON);

        String recordingUrl = recordResponse.get("url").toString();
        CallRecordLog callRecordLogs = callRecordLogRepository.findByCallSid(callSid).orElseThrow(()->new RuntimeException("No call record found for call sid : "+callSid));
        callRecordLogs.setRecordingUrl(recordingUrl);
        callRecordLogRepository.save(callRecordLogs);

        return "<Response>" +
                "<Stream keepCallAlive=\""+ plivoConfig.getKeepAlive() +
                "\" bidirectional=\""+ plivoConfig.getBidirectional() +
                "\" contentType=\"" + plivoConfig.getAudioFormat() + "\">" +
                plivoConfig.getWsUrl() +
                "</Stream>" +
                "</Response>";
    }
}
