package com.cspl.common.gen_ai.speechaiengine.webSocketHandlers;

import com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.factory.IAIAdaptorFactory;
import com.cspl.common.gen_ai.speechaiengine.dto.SessionDetailsDTO;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies.selectors.IAIIncomingEventHandlerFactory;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.IAIOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.Map;

@AllArgsConstructor
@Service
@Slf4j
public class ElevenLabsWebSocketHandler {

    /**
     * The ReactorNettyWebSocketClient instance used to establish WebSocket connections.
     */
    private final ReactorNettyWebSocketClient client;

    /**
     * A map to store active WebSocket sessions.
     */
    private final IAIAdaptorFactory aiAdaptorFactory;

    /**
     * A map to store active WebSocket sessions.
     */
    private final IAIIncomingEventHandlerFactory aiIncomingEventHandlerFactory;

    /**
     * Connects to a WebSocket server at the specified URL and initializes the session.
     * @param url
     * @param providerSession
     * @param initMessage
     * @param aiProvider
     * @return A Mono containing the WebSocket session.
     */
    public Mono<Void> connect(String url, WebSocketSession providerSession , String initMessage, AIProvider aiProvider) {

        Sinks.Many<WebSocketMessage> sink = Sinks.many().multicast().onBackpressureBuffer();

        String providerSessionId = providerSession.getId();

        return client.execute(URI.create(url), session -> {
            log.info("Client WebSocket connected: {}", providerSessionId);

            SessionDetailsLogUtils.messageSinks.put(session.getId(), sink);

            SessionDetailsDTO sessionDetailsDTO = SessionDetailsLogUtils.sessionDetailDtoMap.get(providerSessionId);
            Map<AIProvider,WebSocketSession> aiSessionMap = sessionDetailsDTO.getAiSessionsMap();
            aiSessionMap.put(aiProvider,session);
            sessionDetailsDTO.setAiSessionsMap(aiSessionMap);
            SessionDetailsLogUtils.sessionDetailDtoMap.put(providerSessionId,sessionDetailsDTO);

            SessionDetailsLogUtils.aiToProviderSessionMap.put(session.getId(), providerSession);

            //log.info("init message sent to client {}",initMessage);
            if(Strings.isNotBlank(initMessage)) session.send(Mono.just(session.textMessage(initMessage))).subscribe();

            // Maintain a continuous bidirectional WebSocket session
            return session.send(sink.asFlux()).and( // Keep sending messages as they arrive
                    session.receive()
                            .doOnNext(message -> {
                                log.debug("Client Received message ({}): {}", providerSessionId, message.getPayloadAsText());
                                try {
                                    handleIncomingMessage(message, session, aiProvider);
                                } catch (JsonProcessingException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                            .doFinally(signal -> {
                                log.info("Client WebSocket closed connection with : {} for providerSession {}",aiProvider,providerSessionId);
                                cleanupSession(session.getId());
                            })
            ).then();
        }).then();
    }

    /**
     * Sends a message to the specified WebSocket session.
     * @param session
     * @param aiOutgoingEvent
     * @param aiProvider
     * @throws JsonProcessingException
     */
    public void sendMessage(WebSocketSession session, IAIOutgoingEvent aiOutgoingEvent, AIProvider aiProvider) throws JsonProcessingException {
        String message = aiAdaptorFactory.getAIAdaptor(aiProvider).getSpecificAIOutGoingMessage(aiOutgoingEvent);
        if(Strings.isNotBlank(message) && SessionDetailsLogUtils.messageSinks.containsKey(session.getId())) {
            Sinks.Many<WebSocketMessage> sink = SessionDetailsLogUtils.messageSinks.get(session.getId());

            if (sink != null && session != null) {
                sink.tryEmitNext(session.textMessage(message));
                log.debug("Sent to AI : {}",message);
            } else {
                log.warn("Cannot send message. Session not found: {}", session.getId());
            }
        }
    }

    /**
     * Handles incoming messages from the WebSocket session.
     * @param message
     * @param aiSession
     * @param aiProvider
     * @throws JsonProcessingException
     */
    private void handleIncomingMessage(WebSocketMessage message,WebSocketSession aiSession, AIProvider aiProvider) throws JsonProcessingException {
        aiAdaptorFactory.getAIAdaptor(aiProvider).getCommonAIEvent(message.getPayloadAsText())
                .ifPresent(aiIncomingEvent -> {
                    try {
                        aiIncomingEventHandlerFactory.getEventHandler(aiIncomingEvent.getType()).handleEvent(aiIncomingEvent,aiSession);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * Cleans up the session by removing it from the active sessions map and closing the session.
     * @param sessionId
     */
    private void cleanupSession(String sessionId) {
        SessionDetailsLogUtils.messageSinks.remove(sessionId);
        SessionDetailsLogUtils.aiToProviderSessionMap.get(sessionId).close().subscribe();
        SessionDetailsLogUtils.aiToProviderSessionMap.remove(sessionId);
    }
}
