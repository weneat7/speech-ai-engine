package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;

/**
 * DTO for {@link com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventResponseDto implements Serializable {

    private String id;

    @NotNull
    @NotBlank
    @NotEmpty
    private String toPhoneNumber;

    @NotNull
    private DialerType dialerType;

    @NotNull
    @NotBlank
    private String fromPhoneNumber;

    @NotNull
    private EventStatus eventStatus;

    @Positive
    private Integer maxAttempts;

    @Positive
    private Integer backOffTimeInHours;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private String startTime;

    @DateTimeFormat(pattern = "HH:mm:ss")
    private String endTime;

    @NotNull
    private EventLead eventLead;
}