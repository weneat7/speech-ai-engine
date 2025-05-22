package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderStartEvent;
import com.cspl.common.gen_ai.speechaiengine.services.ISessionDetailService;
import com.cspl.common.gen_ai.speechaiengine.webSocketHandlers.ElevenLabsWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Handler for the START event from the provider.
 */
@Service
@AllArgsConstructor
public class ProviderIncomingStartEventHandler implements IProviderIncomingEventHandler{

    /**
     * Service to handle session details.
     */
    private final ISessionDetailService sessionDetailService;

    /**
     * WebSocket handler for Eleven Labs.
     */
    private final ElevenLabsWebSocketHandler aiWebSocketHandler;


    /**
     * Get the type of the event.
     * @return The type of the event.
     */
    @Override
    public ProviderIncomingEventType getType() {
        return ProviderIncomingEventType.START;
    }

    /**
     * Handle the incoming event from the provider.
     * @param event
     * @param providerSession
     * @throws JsonProcessingException
     */
    @Override
    public void handleProviderIncomingEvent(IProviderIncomingEvent event, WebSocketSession providerSession) throws Exception {

        ProviderStartEvent providerStartEvent = (ProviderStartEvent) event;

        sessionDetailService.processSessionDetailsLogCreation(providerStartEvent.getCallSid(),providerStartEvent.getStreamSid(),providerSession,((ProviderStartEvent) event).getProvider());
    }
}
