package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.dto.EventRecordMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;


@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@Document(collection = "event_records")
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
public class EventRecord extends AbstractBaseAuditableEntity<String> {

    @NotNull
    private String toPhoneNumber;

    @NotNull
    private DialerType dialerType;

    @NotNull
    private String fromPhoneNumber;

    private EventStatus eventStatus;

    private Integer maxAttempts;

    private Integer backOffTimeInMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String dailyStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private String dailyStopTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endDate;

    private LocalDateTime startDateTime;

    private LocalDateTime endDateTime;

    @NotNull
    private String agentId;

    private Map<String, Object> requestData;

    private EventRecordMetadata metaData;
}
