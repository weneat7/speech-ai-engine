package com.cspl.common.gen_ai.speechaiengine.dto.metadata;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Retry metadata that will be embedded in EventRecord
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetryMetadata {

    // Current retry status
    private Retry.Status status = Retry.Status.ELIGIBLE;

    // Attempt counters
    private Integer totalAttempts = 0;
    private Integer dailyAttempts = 0;
    private Integer weeklyAttempts = 0;
    private Integer monthlyAttempts = 0;

    // Timing information
    private LocalDateTime nextRetryTime;
    private LocalDateTime lastAttemptTime;
    private LocalDateTime firstAttemptTime;

    // Current retry configuration
    private Integer currentDelayMinutes = 0;

    // Attempt history
    @Builder.Default
    private List<RetryAttempt> attemptHistory = new ArrayList<>();

    // Last failure information
    private CallStatus lastFailureStatus;
    private Retry.Trigger lastTrigger;
    private String lastFailureReason;
}
