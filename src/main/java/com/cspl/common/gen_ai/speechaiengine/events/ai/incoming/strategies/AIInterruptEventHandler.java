package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.ProviderSender;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.ProviderOutgoingInterruptEvent;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Handler for AI interrupt events
 *
 */
@Service
@AllArgsConstructor
public class AIInterruptEventHandler implements IAIIncomingEventHandler {

    /**
     * ProviderSender instance to send events to the provider
     */
    private final ProviderSender providerSender;

    /**
     * Get the type of the event handler
     * @return AIIncomingEventType
     */
    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.INTERRUPT;
    }

    /**
     * Handle the incoming event
     * @param aiIncomingEvent
     * @param aiSession
     * @throws JsonProcessingException
     */
    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws JsonProcessingException {
        WebSocketSession providerSession = SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId());
        providerSender.sendToSession(ProviderOutgoingInterruptEvent.builder().streamSid(
                SessionDetailsLogUtils
                        .sessionDetailDtoMap
                        .get(providerSession.getId())
                        .getSessionDetailsLog()
                        .getStreamId())
                        .build(),
                providerSession, SessionDetailsLogUtils.sessionDetailDtoMap.get(SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog().getProvider());
    }
}
