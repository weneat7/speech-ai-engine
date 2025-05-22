package com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.factory;

import com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.IAIAdaptor;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Factory class to get the AIAdaptor based on the AIProvider
 */
@Service
@AllArgsConstructor
public class AIAdaptorFactory implements IAIAdaptorFactory {

    /**
     * Map to hold the AIAdaptor instances
     */
    private final Map<String, IAIAdaptor>  AIAdaptorMap;

    /**
     * Method to get the AIAdaptor based on the AIProvider
     * @param aiProvider AIProvider enum
     * @return IAIAdaptor instance
     */
    public IAIAdaptor getAIAdaptor(AIProvider aiProvider){
        return AIAdaptorMap.get(aiProvider.toString() + AppConstants.AI_ADAPTOR_SUFFIX);
    }
}