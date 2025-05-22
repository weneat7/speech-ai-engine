package com.cspl.common.gen_ai.speechaiengine.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

import java.util.HashMap;
import java.util.Map;

@Configuration
@AllArgsConstructor
public class WebSocketConfiguration {
    /**
     * WebSocket client for making WebSocket requests
     */
    private final WebSocketHandler twilioWebSocketHandler;

    /**
     * WebSocket client for making WebSocket requests
     */
    private final WebSocketHandler plivoWebSocketHandler;

    /**
     * WebSocket client for making WebSocket request
     * @return HandlerMapping
     */
    @Bean
    @Order(1)
    public HandlerMapping webSocketHandlerMapping() {
        Map<String, WebSocketHandler> map = new HashMap<>();
        map.put("/ws/twilio", twilioWebSocketHandler);
        map.put("/ws/plivo", plivoWebSocketHandler);
        SimpleUrlHandlerMapping handlerMapping = new SimpleUrlHandlerMapping();
        handlerMapping.setOrder(-1);
        handlerMapping.setUrlMap(map);
        return handlerMapping;
    }

    /**
     * WebSocket client for making WebSocket requests
     * @return WebSocketHandlerAdapter
     */
    @Bean
    public WebSocketHandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
