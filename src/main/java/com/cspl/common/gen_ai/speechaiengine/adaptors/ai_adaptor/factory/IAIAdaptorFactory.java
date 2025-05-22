package com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.factory;

import com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.IAIAdaptor;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;

/**
 * Factory interface to get the AIAdaptor based on the AIProvider
 */
public interface IAIAdaptorFactory {

    /**
     *
     * @param aiProvider
     * @return
     */
    public IAIAdaptor getAIAdaptor(AIProvider aiProvider);
}
