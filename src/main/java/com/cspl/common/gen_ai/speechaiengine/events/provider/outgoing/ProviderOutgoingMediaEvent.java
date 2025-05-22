package com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * This class represents an outgoing media event from the provider.
 */
@Data
@AllArgsConstructor
@Builder
public class ProviderOutgoingMediaEvent implements IProviderOutgoingEvent{

    private String mediaPayload;

    private String streamId;

    private Provider provider;

    /**
     * Get the type of the provider outgoing event.
     * @return
     */
    @Override
    public ProviderOutgoingEventType getType() {
        return ProviderOutgoingEventType.MEDIA;
    }
}
