package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AIBotTranscriptEvent implements IAIIncomingEvent{

    private String botTranscript;

    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.BOT_TRANSCRIPT;
    }
}
