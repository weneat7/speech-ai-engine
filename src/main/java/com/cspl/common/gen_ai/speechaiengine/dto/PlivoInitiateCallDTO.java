package com.cspl.common.gen_ai.speechaiengine.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlivoInitiateCallDTO{

    @JsonProperty("answer_url")
    private String answerUrl;

    @JsonProperty("answer_method")
    private String answerMethod;

    @JsonProperty("hangup_url")
    private String hangupUrl;

    @JsonProperty("hangup_method")
    private String hangupMethod;

    @JsonProperty("to")
    private String to;

    @JsonProperty("from")
    private String from;
}
