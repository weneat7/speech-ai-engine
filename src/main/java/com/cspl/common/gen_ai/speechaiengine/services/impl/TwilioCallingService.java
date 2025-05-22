package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.config.RestClientHelper;
import com.cspl.common.gen_ai.speechaiengine.dto.CallDetailsDto;
import com.cspl.common.gen_ai.speechaiengine.dto.TwilioInitiateCallDTO;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Connect;
import com.twilio.twiml.voice.Hangup;
import com.twilio.twiml.voice.Stream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@AllArgsConstructor
public class TwilioCallingService extends AbstractCallingService {

    private final RestClientHelper restClientHelper;
    private final ObjectMapper objectMapper;
    private final AppProperties.TwilioConfig twilioConfig;

    /**
     * to update status upon call end
     * @param callSid
     * @param status
     * @throws Exception
     */
    @Override
    public void updateCallRecordStatus(String callSid, String status) throws Exception {
        CallRecordLog callRecordLog = callRecordLogRepository.findByCallSid(callSid).orElseThrow(()->new RuntimeException("No Call Record Found for callSid : "+callSid));
        callRecordLog.setCallStatus(CallStatus.getFromValue(status));
        pullCallAndClearRedis(callRecordLog.getEventKey());
        if(callRecordLog.getCallStatus().equals(CallStatus.COMPLETED)) {
            Map<String, Object> callDetails = restClientHelper.call(twilioConfig.getCallDetailUri() + callSid + ".json", Map.of(HttpHeaders.AUTHORIZATION, twilioConfig.getAuthToken()));
            callRecordLog.setCallDetailsDto(objectMapper.convertValue(callDetails, CallDetailsDto.class));
            handleCallEndCompleted(callRecordLog);
        }else {
            callRecordLogRepository.save(callRecordLog);
        }
    }

    /**
     * to initiate a twilio call
     * @param eventRecord
     * @throws Exception
     */
    @Override
    public void initiateCallFlowEvent(EventRecord eventRecord) throws Exception {
            try {
                Map<String, Object> request =
                        objectMapper.convertValue(TwilioInitiateCallDTO
                        .builder()
                        .from(eventRecord.getFromPhoneNumber())
                        .to(eventRecord.getToPhoneNumber())
                        .method("GET")
                        .url(twilioConfig.getAnswerUrl())
                        .statusCallbackMethod("GET")
                        .statusCallback(twilioConfig.getHangupUrl())
                        .record("true")
                        .recordingStatusCallbackMethod("GET")
                        .recordingStatusCallback(twilioConfig.getRecordingUri())
                        .build(), new TypeReference<Map<String, Object>>() {});

                Map<String,String> headers = Map.of(HttpHeaders.AUTHORIZATION, twilioConfig.getAuthToken());
                Map<String,Object> response = restClientHelper.call(twilioConfig.getCallUri(), request ,headers,MediaType.APPLICATION_FORM_URLENCODED);

                handleEventCallMapping(response,eventRecord);
            } catch (Exception ex) {
                log.error("[TwilioCallingService] Error while initiating call", ex);
            }
    }

    /**
     * to get websocket stream configuration
     * @param callSid
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public String configureWebSocketConnection(String callSid) throws JsonProcessingException {
        return new
                VoiceResponse.Builder()
                .connect(new Connect.Builder().stream(new Stream.Builder().url(twilioConfig.getWsUrl()).build()).build())
                .hangup(new Hangup.Builder().build())
                .build().toXml();
    }
}
