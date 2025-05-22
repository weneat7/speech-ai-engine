package com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing;

import com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor.IProviderAdaptorFactory;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.IProviderOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

@Slf4j
@Service
@AllArgsConstructor
public class ProviderSender implements IProviderSender{

    private final IProviderAdaptorFactory providerAdaptorFactory;

    public void sendToSession(IProviderOutgoingEvent providerOutgoingEvent, WebSocketSession providerSession, Provider provider) throws JsonProcessingException {
        String message = providerAdaptorFactory.getProviderAdaptor(provider).convertToSpecificEvent(providerOutgoingEvent);
        Sinks.Many<WebSocketMessage> sink = SessionDetailsLogUtils.messageSinks.get(providerSession.getId());
        if (sink != null) {
            sink.tryEmitNext(providerSession.textMessage(message));
            log.debug("Sent to {} sessionId {}: {}",provider.toString(), providerSession.getId(), message);
        } else {
            log.warn("Session {} not found or closed", providerSession.getId());
        }
    }
}
