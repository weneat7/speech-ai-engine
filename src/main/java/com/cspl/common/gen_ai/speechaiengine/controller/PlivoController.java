package com.cspl.common.gen_ai.speechaiengine.controller;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping(APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.PLIVO_URL)
@CrossOrigin("*")
@AllArgsConstructor
public class PlivoController {

    private final ICallingService plivoCallingService;

    @GetMapping(APIConstants.ANSWER)
    public String createWebSocketConnection(@RequestParam(value = "CallUUID") String callUUID) throws JsonProcessingException {
        return plivoCallingService.configureWebSocketConnection(callUUID);
    }


    @GetMapping(APIConstants.HANGUP)
    public ResponseEntity<Void> onHangUp(@RequestParam("CallUUID") String callUUID, @RequestParam("CallStatus")String status) throws Exception {
        plivoCallingService.updateCallRecordStatus(callUUID,status);
        return ResponseEntity.ok().build();
    }
}