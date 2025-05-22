package com.cspl.common.gen_ai.speechaiengine.dtos.request;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Builder
@Data
@AllArgsConstructor
public class EventRecordRequestDto{

    @NotNull(message = "toPhoneNumber Not Present")
    String toPhoneNumber;

    @NotNull
    DialerType dialerType;

    @NotNull(message = "fromPhoneNumber Not Present")
    String fromPhoneNumber;

    EventStatus eventStatus;
    Integer maxAttempts;
    Integer backOffTimeInMinutes;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime dailyStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime dailyStopTime;
    Map<String,Object> requestData;
    LocalDate startDate;
    LocalDate endDate;
    String zoneId;

    @NotNull(message = "agentId Not Present")
    String agentId;

    AIProvider aiProvider;
}