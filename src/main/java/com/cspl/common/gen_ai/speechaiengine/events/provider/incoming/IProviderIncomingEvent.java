package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming;

/**
 * Interface representing a provider incoming event.
 */
public interface IProviderIncomingEvent {

    /**
     * Get the type of the provider incoming event.
     *
     * @return The type of the provider incoming event.
     */
    public ProviderIncomingEventType getProviderType();
}
