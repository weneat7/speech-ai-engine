package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.cspl.common.gen_ai.speechaiengine.dto.metadata.EventRecordMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventRecordResponseDto{
    String id;
    String toPhoneNumber;
    DialerType dialerType;
    String fromPhoneNumber;
    EventStatus eventStatus;
    Integer maxAttempts;
    Integer backOffTimeInMinutes;
    LocalDate startDate;
    LocalDate endDate;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime dailyStartTime;
    @DateTimeFormat(pattern = "HH:mm")
    LocalTime dailyStopTime;
    EventLead eventLead;
    String agentId;
    EventRecordMetadata metaData;
    String recordingUrl;
}
