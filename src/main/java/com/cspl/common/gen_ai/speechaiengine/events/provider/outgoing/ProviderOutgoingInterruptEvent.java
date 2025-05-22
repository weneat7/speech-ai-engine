package com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ProviderOutgoingInterruptEvent implements IProviderOutgoingEvent{


    private String streamSid;

    private Provider provider;

    /**
     * This class represents an outgoing interrupt event from the provider.
     * @return
     */
    @Override
    public ProviderOutgoingEventType getType() {
        return ProviderOutgoingEventType.INTERRUPT;
    }
}
