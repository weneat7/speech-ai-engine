package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AIToolCallEvent implements IAIIncomingEvent{

    private String toolName;

    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.TOOL_CALL;
    }
}