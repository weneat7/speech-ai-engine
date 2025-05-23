package com.cspl.common.gen_ai.speechaiengine.services.strategies.retries;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;

import java.time.LocalDateTime;

/**
 * Interface for retry strategy implementations
 */
public interface IRetryStrategy {

    /**
     * Calculate the next retry time based on the strategy
     *
     * @param eventRecord The event record being retried
     * @param campaign The campaign containing retry configuration
     * @param callStatus The status that triggered the retry
     * @param trigger The retry trigger reason
     * @return The calculated next retry time
     */
    LocalDateTime calculateNextRetryTime(EventRecord eventRecord, Campaign campaign, CallStatus callStatus, Retry.Trigger trigger);

    /**
     * Check if the event record should be retried based on limits and conditions
     *
     * @param eventRecord The event record to check
     * @param campaign The campaign containing retry configuration
     * @param callStatus The status that triggered the retry
     * @return true if should retry, false otherwise
     */
     boolean shouldRetry(EventRecord eventRecord, Campaign campaign, CallStatus callStatus);

    /**
     * Get the strategy type this implementation handles
     *
     * @return The retry strategy type
     */
    Retry.Strategy getStrategyType();

    /**
     * Validate if the retry configuration is valid for this strategy
     *
     * @param retryConfig The retry configuration to validate
     * @return true if valid, false otherwise
     */
    boolean isConfigurationValid(EventRecordRetryConfiguration retryConfig);
}
