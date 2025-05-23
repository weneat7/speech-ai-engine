package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.dto.metadata.CampaignMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.enums.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZoneId;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "campaign")
public class Campaign extends AbstractBaseAuditableEntity<String> {

        private String name;

        private String description;

        private String agentId;

        private AIProvider aiProvider;

        private CampaignType campaignType;

        private List<CommunicationType> communicationType;

        private CampaignStatus campaignStatus;


        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private String startDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private String endDate;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private String dailyStartTime;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        private String dailyStopTime;

        private DialerType dialer;

        private List<String> fromPhoneNumber;

        private Integer maxAttempts;

        private Integer dailyAttempts;

        private Integer weeklyAttempts;

        private Integer backOffTimeInMinutes;

        private List<String> ruleId;

        @DBRef(lazy = true)
        private List<EventRecordLeadMapping> eventRecordLeadMappings;

        @JsonFormat(shape = JsonFormat.Shape.STRING)
        private ZoneId zoneId;

        private CampaignMetadata metadata;
}
