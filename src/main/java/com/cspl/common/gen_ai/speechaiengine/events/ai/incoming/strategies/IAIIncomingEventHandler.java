package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface IAIIncomingEventHandler {

    /**
     * Get the type of the event handler
     * @return AIIncomingEventType
     */
    public AIIncomingEventType getType();

    /**
     * Handle the incoming event
     * @param aiIncomingEvent
     * @param aiSession
     * @throws JsonProcessingException
     */
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws Exception;
}
