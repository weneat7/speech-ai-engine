package com.cspl.common.gen_ai.speechaiengine.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class RuleCreationDTO {

    @JsonProperty("rule_id")
    private String ruleId;

    @NotNull
    @JsonProperty("tenant_id")
    private String tenantId;

    @NotNull
    @JsonProperty("name")
    private String name;

    @JsonProperty("parameters")
    List<Map<String,Object>> parameters;

    @JsonProperty("source")
    private String source;
}
