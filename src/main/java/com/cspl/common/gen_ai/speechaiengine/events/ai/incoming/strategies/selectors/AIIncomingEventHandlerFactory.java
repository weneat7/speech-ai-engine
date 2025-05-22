package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.selectors;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.IAIIncomingEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class AIIncomingEventHandlerFactory implements IAIIncomingEventHandlerFactory{

    /**
     * Map of AIIncomingEventType to IAIIncomingEventHandler
     */
    private final Map<AIIncomingEventType, IAIIncomingEventHandler> aiIncomingEventHandlerMap;

    /**
     * List of IAIIncomingEventHandler strategies
     */
    private final List<IAIIncomingEventHandler> aiIncomingEventHandlerStrategies;

    /**
     * Get the event handler for the given type
     * @param type
     * @return
     */
    public IAIIncomingEventHandler getEventHandler(AIIncomingEventType type){
        return aiIncomingEventHandlerMap.get(type);
    }

    /**
     * Initialize the factory by populating the map with the strategies
     */
    @PostConstruct
    public void init() {
        for(IAIIncomingEventHandler eventHandler: aiIncomingEventHandlerStrategies) {
            Assert.notNull(eventHandler.getType(), "Type cannot be empty");
            aiIncomingEventHandlerMap.put(eventHandler.getType(),eventHandler);
        }
    }
}
