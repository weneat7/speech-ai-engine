package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
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
public class EventLeadDto implements Serializable {
    @NotNull
    @NotEmpty
    @NotBlank
    private String fileName;
    private Map<String,Object> requestData;
}