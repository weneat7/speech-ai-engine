package com.cspl.common.gen_ai.speechaiengine.webSocketHandlers;

import com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor.TwilioAdaptor;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies.selectors.IProviderIncomingEventHandlerFactory;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@AllArgsConstructor
public class TwilioWebSocketHandler implements WebSocketHandler {

    private final Map<String, Sinks.Many<WebSocketMessage>> activeSessions = new ConcurrentHashMap<>();
    private final TwilioAdaptor twilioAdaptor;
    private final IProviderIncomingEventHandlerFactory providerIncomingEventHandlerFactory;

    /**
     * Handles the WebSocket session for the provider.
     * @param providerSession
     * @return
     */
    @Override
    public Mono<Void> handle(WebSocketSession providerSession) {
        String sessionId = providerSession.getId();
        log.info("Provider WebSocket session started: {}", sessionId);

        Sinks.Many<WebSocketMessage> messageSink = Sinks.many().multicast().onBackpressureBuffer();
        SessionDetailsLogUtils.messageSinks.put(sessionId, messageSink);

        Mono<Void> receiveMessages = providerSession.receive()
                .doOnNext(message -> {
                    log.debug("Received from {}: {}", sessionId, message.getPayloadAsText());
                    try {
                        handleIncomingMessage(message, providerSession);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                })
                .doOnError(error -> log.error("Error from {}: {}", sessionId, error.getMessage()))
                .doFinally(signalType -> {
                    closeSession(sessionId);
                })
                .then();

        // Send messages from sink to the session
        Mono<Void> sendMessages = providerSession.send(messageSink.asFlux());

        // Combine both send and receive operations
        return receiveMessages.and(sendMessages);
    }


    /**
     * Handles incoming messages from the WebSocket session.
     * @param message
     * @param providerSession
     * @throws JsonProcessingException
     */
    private void handleIncomingMessage(WebSocketMessage message , WebSocketSession providerSession) throws JsonProcessingException {
        Optional<IProviderIncomingEvent> providerEventOptional = twilioAdaptor.convertToCommonProviderEvent(message.getPayloadAsText());
        providerEventOptional.ifPresent(providerEvent->{
            try {
                providerIncomingEventHandlerFactory.getProviderIncomingEventHandler(providerEvent.getProviderType())
                        .handleProviderIncomingEvent(providerEvent,providerSession);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Cleans up the session when closed.
     * @param sessionId
     */
    public void closeSession(String sessionId){
        log.info("Session {} closed with Twilio", sessionId);
        Map<AIProvider,WebSocketSession> aiProviderWebSocketSessionMap = SessionDetailsLogUtils.sessionDetailDtoMap.get(sessionId).getAiSessionsMap();
        for(WebSocketSession aiSession:aiProviderWebSocketSessionMap.values()) aiSession.close().subscribe();
        SessionDetailsLogUtils.messageSinks.remove(sessionId);
        SessionDetailsLogUtils.aiToProviderSessionMap.remove(sessionId);
    }
}
