package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.selectors;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.IAIIncomingEventHandler;

/**
 * Factory interface for creating AIIncomingEventHandler instances based on the event type.
 * This interface is used to decouple the creation of event handlers from their usage.
 */
public interface IAIIncomingEventHandlerFactory {

    /**
     * Get the event handler for the given event type.
     * @param type
     * @return
     */
    public IAIIncomingEventHandler getEventHandler(AIIncomingEventType type);
}
