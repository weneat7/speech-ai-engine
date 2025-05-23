package com.cspl.common.gen_ai.speechaiengine.dto;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Transcription;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
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

    private List<Transcription> transcriptions;

    private String conversationId;

    private Map<String, Object> conversationResult;
}
