package com.cspl.common.gen_ai.speechaiengine.services.strategies.retries;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.RetryMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Exponential retry strategy: delay doubles each attempt
 * Formula: delay = baseDelay * (multiplier ^ attemptNumber)
 */
@Component
@Slf4j
public class ExponentialRetryStrategy extends BaseRetryStrategy {

    @Override
    public LocalDateTime calculateNextRetryTime(EventRecord eventRecord, Campaign campaign,
                                                CallStatus callStatus, Retry.Trigger trigger) {

        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);
        RetryMetadata retryMetadata = getRetryMetadata(eventRecord);

        // Calculate exponential delay: baseDelay * (multiplier ^ attempts)
        int totalAttempts = retryMetadata.getTotalAttempts();
        int baseDelay = config.getBaseDelayInMinutes();
        double multiplier = config.getMultiplier();

        // Calculate exponential delay
        long delay = Math.round(baseDelay * Math.pow(multiplier, totalAttempts));

        // Cap at maximum delay
        delay = Math.min(delay, config.getExponentialMaxDelayInMinutes());

        LocalDateTime nextRetryTime = LocalDateTime.now().plusMinutes(delay);

        log.debug("Exponential strategy calculated next retry time: {} (delay: {} InMinutes, attempt: {}, base: {}, multiplier: {})",
                nextRetryTime, delay, totalAttempts + 1, baseDelay, multiplier);

        return nextRetryTime;
    }

    @Override
    public Retry.Strategy getStrategyType() {
        return Retry.Strategy.EXPONENTIAL;
    }

    @Override
    public boolean isConfigurationValid(EventRecordRetryConfiguration retryConfig) {
        return retryConfig != null &&
                retryConfig.getBaseDelayInMinutes() != null && retryConfig.getBaseDelayInMinutes() > 0 &&
                retryConfig.getMultiplier() != null && retryConfig.getMultiplier() > 1.0 &&
                retryConfig.getExponentialMaxDelayInMinutes() != null && retryConfig.getExponentialMaxDelayInMinutes() > 0 &&
                retryConfig.getMaxAttempts() != null && retryConfig.getMaxAttempts() > 0;
    }

    @Override
    protected EventRecordRetryConfiguration createDefaultConfiguration(Campaign campaign) {
        EventRecordRetryConfiguration baseConfig = super.createDefaultConfiguration(campaign);
        baseConfig.setBaseDelayInMinutes(15);
        baseConfig.setMultiplier(2.0);
        baseConfig.setExponentialMaxDelayInMinutes(720); // 12 hours max
        return baseConfig;
    }
}