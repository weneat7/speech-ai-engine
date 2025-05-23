package com.cspl.common.gen_ai.speechaiengine.config;


import com.cspl.common.gen_ai.speechaiengine.models.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventRecordRetryConfiguration {

    // Basic retry limits
    private Integer maxAttempts = 3;
    private Integer dailyAttempts = 2;
    private Integer weeklyAttempts = 5;
    private Integer monthlyAttempts = 10;

    // Strategy configuration
    private Retry.Strategy strategy = Retry.Strategy.LINEAR;

    // Linear strategy parameters
    private Integer initialDelayInMinutes = 30;      // First retry after 30 minutes
    private Integer incrementDelayInMinutes = 30;    // Add 30 minutes each attempt
    private Integer maxDelayInMinutes = 480;         // Max 8 hours delay

    // Exponential strategy parameters
    private Integer baseDelayInMinutes = 15;         // Base delay for exponential
    private Double multiplier = 2.0;               // Multiply by 2 each time
    private Integer exponentialMaxDelayInMinutes = 720; // Max 12 hours delay

    // Which call statuses should trigger retries
    private List<CallStatus> retryableStatuses = List.of(CallStatus.BUSY, CallStatus.NO_ANSWER);

    // Mapping of call status to retry trigger for better tracking
    private Map<CallStatus, Retry.Trigger> statusToTriggerMapping = Map.of(
            CallStatus.BUSY, Retry.Trigger.BUSY_SIGNAL,
            CallStatus.NO_ANSWER, Retry.Trigger.NO_ANSWER,
            CallStatus.FAILED, Retry.Trigger.CALL_FAILED
    );

    // Whether to respect campaign time windows
    private Boolean respectTimeWindows = true;

    // Whether retry system is enabled for this campaign
    private Boolean enabled = true;
}
