package com.cspl.common.gen_ai.speechaiengine.dtos.request;

import com.cspl.common.gen_ai.speechaiengine.models.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.*;
import java.util.List;

/**
 * DTO for {@link com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignRequestDto implements Serializable {

    private String name;

    private String description;

    private String agentId;

    private CampaignType campaignType;

    private CampaignStatus campaignStatus;

    private List<CommunicationType> communicationType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dailyStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dailyStopTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private ZoneId zoneId;

    private DialerType dialer;
    private Integer maxAttempts;
    private Integer dailyAttempts;
    private Integer weeklyAttempts;
    private List<String> ruleId;
    private Integer backOffTimeInMinutes;
    private List<String> fromPhoneNumber;
    private AIProvider aiProvider;
}