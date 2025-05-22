package com.cspl.common.gen_ai.speechaiengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TwilioInitiateCallDTO {

    @JsonProperty("Method")
    private String method;

    @JsonProperty("Url")
    private String url;

    @JsonProperty("Record")
    private String record;

    @JsonProperty("StatusCallback")
    private String statusCallback;

    @JsonProperty("StatusCallbackMethod")
    private String statusCallbackMethod;

    @JsonProperty("RecordingStatusCallback")
    private String recordingStatusCallback;

    @JsonProperty("RecordingStatusCallbackMethod")
    private String recordingStatusCallbackMethod;

    @JsonProperty("To")
    private String to;

    @JsonProperty("From")
    private String from;
}
