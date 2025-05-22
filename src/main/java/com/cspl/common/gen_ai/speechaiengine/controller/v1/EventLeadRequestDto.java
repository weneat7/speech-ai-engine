package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class EventLeadRequestDto implements Serializable {
    private Map<String,Object> requestData;
}