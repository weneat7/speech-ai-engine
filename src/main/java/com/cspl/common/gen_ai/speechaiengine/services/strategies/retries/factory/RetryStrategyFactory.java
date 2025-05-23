package com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.factory;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.IRetryStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory to get appropriate retry strategy based on configuration
 */
@Service
@AllArgsConstructor
@Slf4j
public class RetryStrategyFactory implements IRetryStrategyFactory {

    private final List<IRetryStrategy> retryStrategies;
    private final Map<Retry.Strategy, IRetryStrategy> strategyMap = new HashMap<>();

    @PostConstruct
    public void initialize() {
        for (IRetryStrategy strategy : retryStrategies) {
            strategyMap.put(strategy.getStrategyType(), strategy);
            log.info("Registered retry strategy: {}", strategy.getStrategyType());
        }
    }

    @Override
    public IRetryStrategy getStrategy(Campaign campaign) {
        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);
        Retry.Strategy strategyType = config.getStrategy();

        IRetryStrategy strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            log.warn("Strategy {} not found, falling back to LINEAR", strategyType);
            strategy = strategyMap.get(Retry.Strategy.LINEAR);
        }

        // Validate configuration for the strategy
        if (!strategy.isConfigurationValid(config)) {
            log.warn("Invalid retry configuration for strategy {}, using default LINEAR", strategyType);
            return strategyMap.get(Retry.Strategy.LINEAR);
        }

        return strategy;
    }

    @Override
    public IRetryStrategy getStrategy(Retry.Strategy strategyType) {
        IRetryStrategy strategy = strategyMap.get(strategyType);
        if (strategy == null) {
            log.warn("Strategy {} not found, falling back to LINEAR", strategyType);
            return strategyMap.get(Retry.Strategy.LINEAR);
        }
        return strategy;
    }

    private EventRecordRetryConfiguration getRetryConfiguration(Campaign campaign) {
        if (campaign.getMetadata() != null && campaign.getMetadata().getEventRecordRetryConfiguration() != null) {
            return campaign.getMetadata().getEventRecordRetryConfiguration();
        }

        // Default configuration for backward compatibility
        return EventRecordRetryConfiguration.builder()
                .maxAttempts(3)
                .dailyAttempts(2)
                .weeklyAttempts(5)
                .monthlyAttempts(10)
                .initialDelayInMinutes(30)
                .incrementDelayInMinutes(30)
                .maxDelayInMinutes(480)
                .strategy(Retry.Strategy.LINEAR)
                .enabled(true)
                .respectTimeWindows(true)
                .build();
    }
}

