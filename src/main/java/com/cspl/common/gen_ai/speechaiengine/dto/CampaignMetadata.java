package com.cspl.common.gen_ai.speechaiengine.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CampaignMetadata {
   private Long totalLeads = 0L;
   private Long completedLeads = 0L;
   private Long failedLeads = 0L;
   private Long pendingLeads = 0L;
   private Long inProgressLeads = 0L;
   private Long pickedLeads = 0L;
   private Double totalDurationInSeconds = 0D;
   private Double avgDurationInSeconds = 0D;
   private Double pickUpRate = 0D;
}
