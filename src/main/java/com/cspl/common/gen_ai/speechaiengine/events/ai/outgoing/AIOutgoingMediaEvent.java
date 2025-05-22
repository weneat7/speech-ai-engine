package com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AIOutgoingMediaEvent implements IAIOutgoingEvent{

    private String mediaPayload;

    @Override
    public AIOutgoingEventType getType() {
        return AIOutgoingEventType.MEDIA;
    }
}
