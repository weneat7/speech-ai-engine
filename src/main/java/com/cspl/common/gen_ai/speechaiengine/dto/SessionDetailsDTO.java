package com.cspl.common.gen_ai.speechaiengine.dto;

import com.cspl.common.gen_ai.speechaiengine.models.entities.SessionDetailsLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Data
@Builder
public class SessionDetailsDTO {
    Map<AIProvider, WebSocketSession> aiSessionsMap;
    SessionDetailsLog sessionDetailsLog;
    List<String> toolCalls;
}
