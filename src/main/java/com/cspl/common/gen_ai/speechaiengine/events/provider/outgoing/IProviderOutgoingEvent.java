package com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing;

/**
 * Interface representing a provider outgoing event.
 */
public interface IProviderOutgoingEvent {

    /**
     * Get the type of the provider outgoing event.
     * @return The type of the provider outgoing event.
     */
    public ProviderOutgoingEventType getType();
}
