package com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing;

import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.IProviderOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Interface for sending events to the provider.
 */
public interface IProviderSender {

    /**
     * Send the event to the provider session.
     * @param providerOutgoingEvent
     * @param providerSession
     * @param provider
     * @throws JsonProcessingException
     */
    public void sendToSession(IProviderOutgoingEvent providerOutgoingEvent, WebSocketSession providerSession, Provider provider) throws JsonProcessingException;

}
