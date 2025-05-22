package com.cspl.common.gen_ai.speechaiengine.controller;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.TWILIO_URL)
@Slf4j
@AllArgsConstructor
public class TwilioController {

    private final ICallingService twilioCallingService;
    private final AppProperties.TwilioConfig twilioConfig;

    @GetMapping(value = APIConstants.RECORDING)
    public ResponseEntity<Void> recordingStatus(@RequestParam("RecordingUrl") String RecordingUrl,@RequestParam("CallSid") String CallSid, @RequestParam("RecordingStatus") String RecordingStatus){
        twilioCallingService.handleRecording(RecordingUrl,CallSid,RecordingStatus,twilioConfig.getAuthToken());
        return ResponseEntity.ok().build();
    }
    @GetMapping(APIConstants.ANSWER)
    public String createWebSocketConnection(@RequestParam("CallSid") String callSid) throws JsonProcessingException {
        return twilioCallingService.configureWebSocketConnection(callSid);
    }

    @GetMapping(APIConstants.HANGUP)
    public ResponseEntity<Void> onHangUp(@RequestParam("CallSid") String callSid,@RequestParam("CallStatus")String status) throws Exception {
        twilioCallingService.updateCallRecordStatus(callSid,status);
        return ResponseEntity.ok().build();
    }
}
