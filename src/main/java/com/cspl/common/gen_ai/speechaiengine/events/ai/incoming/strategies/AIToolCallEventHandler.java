package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Handler for tool call events
 */
@Service
@AllArgsConstructor
public class AIToolCallEventHandler implements IAIIncomingEventHandler {

    /**
     * Get the type of the event handler
     * @return
     */
    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.TOOL_CALL;
    }

    /**
     * Handle the incoming event
     * @param aiIncomingEvent
     * @param aiSession
     */
    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) {

    }
}
