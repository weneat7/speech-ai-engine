package com.cspl.common.gen_ai.speechaiengine.dto;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallRecordingLogDTO {

    private String callSid;

    private CallStatus callStatus;

    private String recordingUrl;

    private String encodedAuth;

    private CallDetailsDto callDetailsDto;

    private Map<String, Object> conversationResult;
}
