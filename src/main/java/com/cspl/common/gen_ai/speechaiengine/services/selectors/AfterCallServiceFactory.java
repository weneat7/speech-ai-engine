package com.cspl.common.gen_ai.speechaiengine.services.selectors;


import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.services.IAfterCallService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AfterCallServiceFactory implements IAfterCallServiceFactory {

    Map<String, IAfterCallService> afterCallServiceMap;

    /**
     * Get the AfterCallService based on the AiProvider
     *
     * @param aiProvider
     * @return
     */
    @Override
    public Optional<IAfterCallService> getAfterCallServiceByAiProvider(AIProvider aiProvider) {
        switch (aiProvider) {
            case ELEVEN_LABS -> {
                return Optional.of(afterCallServiceMap.get("elevenLabsAfterCallService"));
            }
            default -> {
                return Optional.empty();
            }
        }
    }

}
