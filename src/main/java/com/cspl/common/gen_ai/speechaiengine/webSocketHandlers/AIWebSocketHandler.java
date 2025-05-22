package com.cspl.common.gen_ai.speechaiengine.webSocketHandlers;

import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@AllArgsConstructor
public class AIWebSocketHandler {

    private final ReactorNettyWebSocketClient client;

    private final Map<String, WebSocketSession> activeSessions = new ConcurrentHashMap<>();

    private final Map<String, Sinks.Many<WebSocketMessage>> messageSinks = new ConcurrentHashMap<>();

    /**
     * Connects to a WebSocket server at the specified URL and initializes the session.
     *
     * @param url               The WebSocket server URL.
     * @param providerSessionId The session ID of the provider.
     * @param initMessage       The initial message to send upon connection.
     * @return A Mono containing the WebSocket session.
     */
    public Mono<WebSocketSession> connect(String url, String providerSessionId , String initMessage) {
        Sinks.Many<WebSocketMessage> sink = Sinks.many().multicast().onBackpressureBuffer();

        return client.execute(URI.create(url), session -> {
            session.send(Mono.just(session.textMessage(initMessage))).subscribe();
            log.info("Client WebSocket connected: {}", providerSessionId);
            activeSessions.put(providerSessionId, session);
            messageSinks.put(session.getId(), sink);
            List<WebSocketSession> activeAiSessions = SessionDetailsLogUtils.activeProviderSessionMap.getOrDefault(providerSessionId,new ArrayList<>());
            activeAiSessions.add(session);
            SessionDetailsLogUtils.activeProviderSessionMap.put(providerSessionId, activeAiSessions);
            return session.receive()
                    .doOnNext(message -> {
                        log.debug("Client Received message ({}): {}",
                                providerSessionId, message.getPayloadAsText());
                        handleIncomingMessage(providerSessionId, message);
                    })
                    .doFinally(signal -> {
                        log.debug("Client WebSocket closed: {}", providerSessionId);
                        cleanupSession(providerSessionId);
                    })
                    .then(Mono.never());
        }).then(Mono.fromSupplier(() -> activeSessions.get(providerSessionId)));
    }

    /**
     * Sends a message to the specified WebSocket session.
     * @param session
     * @param message
     */
    public void sendMessage(WebSocketSession session, String message) {
        Sinks.Many<WebSocketMessage> sink = messageSinks.get(session.getId());

        if (sink != null && session != null) {
            sink.tryEmitNext(session.textMessage(message));
            log.info("Client Sent message ({}): {}", session.getId(), message);
        } else {
            log.warn("Cannot send message. Session not found: {}", session.getId());
        }
    }

    /**
     * Handles incoming messages from the WebSocket session.
     * @param sessionId
     * @param message
     */
    private void handleIncomingMessage(String sessionId, WebSocketMessage message) {
        log.info("Processing client message ({}): {}", sessionId, message.getPayloadAsText());
    }

    /**
     * Cleans up the session when closed.
     * @param sessionId
     */
    private void cleanupSession(String sessionId) {
        activeSessions.remove(sessionId);
        messageSinks.remove(sessionId);
    }
}