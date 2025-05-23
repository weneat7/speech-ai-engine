package com.cspl.common.gen_ai.speechaiengine.services.strategies.retries;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.RetryMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import lombok.extern.slf4j.Slf4j;

/**
 * Base abstract class for retry strategies with common functionality
 */
@Slf4j
public abstract class BaseRetryStrategy implements IRetryStrategy {

    @Override
    public boolean shouldRetry(EventRecord eventRecord, Campaign campaign, CallStatus callStatus) {
        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);

        // Check if retry is enabled
        if (!config.getEnabled()) {
            log.debug("Retry disabled for campaign: {}", campaign.getId());
            return false;
        }

        // Check if this call status is retryable
        if (!config.getRetryableStatuses().contains(callStatus)) {
            log.debug("Call status {} is not retryable for campaign: {}", callStatus, campaign.getId());
            return false;
        }

        RetryMetadata retryMetadata = getRetryMetadata(eventRecord);

        // Check attempt limits
        boolean withinLimits = retryMetadata.getTotalAttempts() < config.getMaxAttempts() &&
                retryMetadata.getDailyAttempts() < config.getDailyAttempts() &&
                retryMetadata.getWeeklyAttempts() < config.getWeeklyAttempts() &&
                retryMetadata.getMonthlyAttempts() < config.getMonthlyAttempts();

        if (!withinLimits) {
            log.debug("Retry limits exceeded for event: {} (total: {}/{}, daily: {}/{}, weekly: {}/{}, monthly: {}/{})",
                    eventRecord.getId(),
                    retryMetadata.getTotalAttempts(), config.getMaxAttempts(),
                    retryMetadata.getDailyAttempts(), config.getDailyAttempts(),
                    retryMetadata.getWeeklyAttempts(), config.getWeeklyAttempts(),
                    retryMetadata.getMonthlyAttempts(), config.getMonthlyAttempts());
        }

        return withinLimits;
    }

    /**
     * Get retry configuration with fallback to legacy fields
     */
    protected EventRecordRetryConfiguration getRetryConfiguration(Campaign campaign) {
        if (campaign.getMetadata().getEventRecordRetryConfiguration() != null) {
            return campaign.getMetadata().getEventRecordRetryConfiguration();
        }

        // Fallback to legacy fields for backward compatibility
        return createDefaultConfiguration(campaign);
    }

    /**
     * Get retry metadata with null safety
     */
    protected RetryMetadata getRetryMetadata(EventRecord eventRecord) {
        if (eventRecord.getMetaData() == null || eventRecord.getMetaData().getRetryMetadata() == null) {
            return RetryMetadata.builder().build();
        }
        return eventRecord.getMetaData().getRetryMetadata();
    }

    /**
     * Create default configuration based on strategy type - subclasses can override
     */
    protected EventRecordRetryConfiguration createDefaultConfiguration(Campaign campaign) {
        return EventRecordRetryConfiguration.builder()
                .maxAttempts(campaign.getMaxAttempts() != null ? campaign.getMaxAttempts() : 3)
                .dailyAttempts(campaign.getDailyAttempts() != null ? campaign.getDailyAttempts() : 2)
                .weeklyAttempts(campaign.getWeeklyAttempts() != null ? campaign.getWeeklyAttempts() : 5)
                .monthlyAttempts(10)
                .strategy(getStrategyType())
                .enabled(true)
                .respectTimeWindows(true)
                .build();
    }
}