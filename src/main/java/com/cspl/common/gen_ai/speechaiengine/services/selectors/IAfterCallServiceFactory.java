package com.cspl.common.gen_ai.speechaiengine.services.selectors;

import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.services.IAfterCallService;

import java.util.Optional;

public interface IAfterCallServiceFactory {

    /**
     * Get the AfterCallService based on the AiProvider
     *
     * @param aiProvider
     * @return
     */
    Optional<IAfterCallService> getAfterCallServiceByAiProvider(AIProvider aiProvider);
}
