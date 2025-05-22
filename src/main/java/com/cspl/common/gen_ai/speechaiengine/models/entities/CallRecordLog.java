package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.dto.CallDetailsDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "call_record_logs")
public class CallRecordLog extends AbstractBaseAuditableEntity<String> {

    private String callSid;

    private String phoneNumber;

    private String from;

    private String eventKey;

    private CallStatus callStatus;

    private String recordingUrl;

    private List<Transcription> transcriptions;

    private CallDetailsDto callDetailsDto;

    private String conversationId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Map<String, Object> conversationResult;
}
