package com.cspl.common.gen_ai.speechaiengine.dto.metadata;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Individual retry attempt record
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryAttempt {
    private Integer attemptNumber;
    private LocalDateTime attemptTime;
    private CallStatus callStatus;
    private Retry.Trigger trigger;
    private String reason;
    private Long callDurationSeconds;
}