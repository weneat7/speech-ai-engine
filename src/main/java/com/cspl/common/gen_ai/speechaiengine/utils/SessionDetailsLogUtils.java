package com.cspl.common.gen_ai.speechaiengine.utils;

import com.cspl.common.gen_ai.speechaiengine.dto.SessionDetailsDTO;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Sinks;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author priyanshyu.gupta
 * Utility class to manage session details and logs.
 */
public class SessionDetailsLogUtils {

    /**
     * Map to store active provider sessions with a list of active AI sessions.
     */
    public static final Map<String, List<WebSocketSession>> activeProviderSessionMap = new ConcurrentHashMap<>(); //provider session with list of active ai sessions

    /**
     * Map to store active AI sessions with their corresponding provider session.
     */
    public static final Map<String,WebSocketSession> aiToProviderSessionMap = new ConcurrentHashMap<>(); // ai session id to provider session

    /**
     *  Map to store active AI sessions with their corresponding provider session.
     */
    public static final Map<String, Sinks.Many<WebSocketMessage>> messageSinks = new ConcurrentHashMap<>(); // sessions buffers for sending messages as sink

    /**
     * Map to store session details DTOs with their corresponding session IDs.
     */
    public static final Map<String, SessionDetailsDTO> sessionDetailDtoMap = new ConcurrentHashMap<>();
}
