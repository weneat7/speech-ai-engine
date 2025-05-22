package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class AIInterruptEvent implements IAIIncomingEvent{

    private final AIIncomingEventType type = AIIncomingEventType.INTERRUPT;

    @Override
    public AIIncomingEventType getType() {
        return type;
    }
}
