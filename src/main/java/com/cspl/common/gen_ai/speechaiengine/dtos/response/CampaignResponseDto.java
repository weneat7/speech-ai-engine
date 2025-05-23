package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.cspl.common.gen_ai.speechaiengine.dto.metadata.CampaignMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CommunicationType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

/**
 * DTO for {@link Campaign}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CampaignResponseDto implements Serializable {

    @NotNull(message = "Id can't be null or empty for Campaign")
    private String id;

    @NotNull(message = "Name can't be null or empty for Campaign")
    @Size
    @NotEmpty(message = "Name can't be null or empty for Campaign")
    @NotBlank(message = "Name can't be null or empty for Campaign")
    private String name;
    @NotNull(message = "Description can't be null or empty")
    @NotEmpty
    @NotBlank
    private String description;
    private CampaignType campaignType;
    private List<CommunicationType> communicationType;
    private CampaignStatus campaignStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private DialerType dialer;
    private Integer maxAttempts;
    private Integer dailyAttempts;
    private Integer weeklyAttempts;
    private List<String> ruleId;
    private List<String> fromPhoneNumber;
    private Integer backOffTimeInMinutes;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dailyStartTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime dailyStopTime;

    private ZoneId zoneId;

    private CampaignMetadata metadata;

    @NotNull(message = "agentId cannot be null")
    private String agentId;

    private String createdAt;
}