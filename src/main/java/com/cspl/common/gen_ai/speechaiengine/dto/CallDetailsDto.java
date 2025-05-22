package com.cspl.common.gen_ai.speechaiengine.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CallDetailsDto {

    @JsonAlias({"duration", "call_duration"})
    private Integer duration;

    @JsonAlias({"billed_duration", "bill_duration"})
    private Integer billedDuration;

    @JsonAlias({"price", "total_amount"})
    private Double cost;

    @JsonAlias({"start_time", "answer_time"})
    private String answerTime;

    @JsonAlias({"end_time"})
    private String endTime;

}
