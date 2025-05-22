package com.cspl.common.gen_ai.speechaiengine.webSocketHandlers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@AllArgsConstructor
public class ProviderWebSocketHandler implements WebSocketHandler {

    private static final Map<String, Sinks.Many<WebSocketMessage>> activeSessions = new ConcurrentHashMap<>();
    private static final Map<String, List<WebSocketSession>> aiSessionsMap = new ConcurrentHashMap<>();
    private final AIWebSocketHandler aiWebSocketHandler;

    @Override
    public Mono<Void> handle(WebSocketSession providerSession) {
        String sessionId = providerSession.getId();
        log.info("Provider WebSocket session started: {}", sessionId);

        Sinks.Many<WebSocketMessage> messageSink = Sinks.many().multicast().onBackpressureBuffer();
        activeSessions.put(sessionId, messageSink);

        List<WebSocketSession> aiSessionsList = aiSessionsMap.getOrDefault(providerSession.getId(),new ArrayList<>());

        aiSessionsMap.put(providerSession.getId(),aiSessionsList);
//        aiWebSocketHandler.connect("wss://api.elevenlabs.io/v1/convai/conversation?agent_id=TmiisAuWESLdkLqEd9jD", providerSession.getId(),"{\n" +
//                        "                        \"type\": \"conversation_initiation_client_data\",\n" +
//                                "                        \"dynamic_variables\":{\n" +
//                                "                            \"customer_name\":\"abc\"\n" +
//                                "                        }\n" +
//                                "}")
//                .doOnSuccess(session -> {
//                    log.info("session of client closed {}",session.getId());
//                })
//                .doFinally(signal->{
//
//                })
//                .subscribe();

        sendToSession("hello connected", providerSession);

        // Handle incoming messages from the provider session
        Mono<Void> receiveMessages = providerSession.receive()
                .doOnNext(message -> {
                    log.info("Received from {}: {}", sessionId, message.getPayloadAsText());
                    handleIncomingMessage(message, providerSession);
                })
                .doOnError(error -> log.error("Error from {}: {}", sessionId, error.getMessage()))
                .doFinally(signalType -> {
                    log.info("Session {} closed", sessionId);
                    activeSessions.remove(sessionId);
                })
                .then();

        // Send messages from sink to the session
        Mono<Void> sendMessages = providerSession.send(messageSink.asFlux());

        // Combine both send and receive operations
        return Mono.when(receiveMessages, sendMessages);
    }


    public static void sendToSession(String message, WebSocketSession providerSession) {
        Sinks.Many<WebSocketMessage> sink = activeSessions.get(providerSession.getId());
        if (sink != null) {
            sink.tryEmitNext(providerSession.textMessage(message));
            log.info("Sent to {}: {}", providerSession.getId(), message);
        } else {
            log.warn("Session {} not found or closed", providerSession.getId());
        }
    }

    private void handleIncomingMessage(WebSocketMessage message , WebSocketSession providerSession){
        log.info("Provider message received : {}",message.getPayload());
        sendToSession("recieved "+message.getPayloadAsText(),providerSession);
    }
}
