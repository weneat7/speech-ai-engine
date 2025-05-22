package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ProviderStartEvent implements IProviderIncomingEvent {

    private String callSid;

    private String streamSid;

    private Provider provider;

    @Override
    public ProviderIncomingEventType getProviderType() {
        return ProviderIncomingEventType.START;
    }
}
