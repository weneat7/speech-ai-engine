package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.IAIIncomingEventHandler;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.selectors.IAIIncomingEventHandlerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.Optional;

@AllArgsConstructor
@Service
public class AIIncomingEventSelector {

    /**
     * Factory to get the event handler
     */
    private final IAIIncomingEventHandlerFactory aiIncomingEventHandlerFactory;

    /**
     * Select the event handler based on the type of the event
     * @param aiIncomingEvent
     * @param aiSession
     * @return
     * @throws JsonProcessingException
     */
    public Optional<IAIIncomingEventHandler> handleAIIncomingEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws JsonProcessingException {
        AIIncomingEventType type = aiIncomingEvent.getType();
        switch (type){
            case MEDIA -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.MEDIA));}
            case USER_TRANSCRIPT -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.USER_TRANSCRIPT));}
            case BOT_TRANSCRIPT -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.BOT_TRANSCRIPT));}
            case INTERRUPT -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.INTERRUPT));}
            case TOOL_CALL -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.TOOL_CALL));}
            case AI_INITIATION -> {return Optional.of(aiIncomingEventHandlerFactory.getEventHandler(AIIncomingEventType.AI_INITIATION));}
        }
        return Optional.empty();
    }

}