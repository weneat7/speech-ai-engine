
package com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.factory;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.IRetryStrategy;

public interface IRetryStrategyFactory {
    /**
     * Get the appropriate retry strategy based on the campaign configuration
     *
     * @param campaign The campaign containing retry configuration
     * @return The appropriate retry strategy
     */
    IRetryStrategy getStrategy(Campaign campaign);

    /**
     * Get the retry strategy based on the strategy type
     *
     * @param strategyType The strategy type to get
     * @return The corresponding retry strategy
     */
    IRetryStrategy getStrategy(Retry.Strategy strategyType);
}