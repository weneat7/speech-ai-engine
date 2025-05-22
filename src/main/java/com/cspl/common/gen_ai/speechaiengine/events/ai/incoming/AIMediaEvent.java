package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AIMediaEvent implements IAIIncomingEvent {

    private String mediaPayload;

    @Override
    public AIIncomingEventType getType() {
        return  AIIncomingEventType.MEDIA;
    }
}
