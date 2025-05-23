package com.cspl.common.gen_ai.speechaiengine.services.retry.strategies;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.RetryMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.BaseRetryStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Linear retry strategy: delay increases by fixed amount each attempt
 * Formula: delay = initialDelay + (attemptNumber * incrementDelay)
 */
@Component
@Slf4j
public class LinearRetryStrategy extends BaseRetryStrategy {

    @Override
    public LocalDateTime calculateNextRetryTime(EventRecord eventRecord, Campaign campaign,
                                                CallStatus callStatus, Retry.Trigger trigger) {

        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);
        RetryMetadata retryMetadata = getRetryMetadata(eventRecord);

        // Calculate linear delay: initialDelay + (attempts * increment)
        int totalAttempts = retryMetadata.getTotalAttempts();
        int delay = config.getInitialDelayInMinutes() + (totalAttempts * config.getIncrementDelayInMinutes());

        // Cap at maximum delay
        delay = Math.min(delay, config.getMaxDelayInMinutes());

        LocalDateTime nextRetryTime = LocalDateTime.now().plusMinutes(delay);

        log.debug("Linear strategy calculated next retry time: {} (delay: {} minutes, attempt: {})",
                nextRetryTime, delay, totalAttempts + 1);

        return nextRetryTime;
    }

    @Override
    public Retry.Strategy getStrategyType() {
        return Retry.Strategy.LINEAR;
    }

    @Override
    public boolean isConfigurationValid(EventRecordRetryConfiguration retryConfig) {
        return retryConfig != null &&
                retryConfig.getInitialDelayInMinutes() != null && retryConfig.getInitialDelayInMinutes() > 0 &&
                retryConfig.getIncrementDelayInMinutes() != null && retryConfig.getIncrementDelayInMinutes() > 0 &&
                retryConfig.getMaxDelayInMinutes() != null && retryConfig.getMaxDelayInMinutes() > 0 &&
                retryConfig.getMaxAttempts() != null && retryConfig.getMaxAttempts() > 0;
    }

    @Override
    protected EventRecordRetryConfiguration createDefaultConfiguration(Campaign campaign) {
        EventRecordRetryConfiguration baseConfig = super.createDefaultConfiguration(campaign);
        baseConfig.setInitialDelayInMinutes(campaign.getBackOffTimeInMinutes() != null ? campaign.getBackOffTimeInMinutes() : 30);
        baseConfig.setIncrementDelayInMinutes(30);
        baseConfig.setMaxDelayInMinutes(480);
        return baseConfig;
    }
}