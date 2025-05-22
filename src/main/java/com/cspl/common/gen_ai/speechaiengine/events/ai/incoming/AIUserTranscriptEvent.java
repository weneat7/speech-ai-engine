package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class AIUserTranscriptEvent implements IAIIncomingEvent{

    private String userTranscript;

    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.USER_TRANSCRIPT;
    }
}
