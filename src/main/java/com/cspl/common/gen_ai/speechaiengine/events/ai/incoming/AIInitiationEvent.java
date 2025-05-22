package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AIInitiationEvent implements IAIIncomingEvent {

    String conversationId;

    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.AI_INITIATION;
    }
}
