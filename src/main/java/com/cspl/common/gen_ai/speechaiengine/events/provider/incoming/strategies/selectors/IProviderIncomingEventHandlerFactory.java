package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies.selectors;

import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies.IProviderIncomingEventHandler;

/**
 * Factory interface for creating instances of IProviderIncomingEventHandler.
 */
public interface IProviderIncomingEventHandlerFactory {

    /**
     * Get the appropriate IProviderIncomingEventHandler based on the event type.
     *
     * @param type The type of the provider incoming event.
     * @return An instance of IProviderIncomingEventHandler for the specified event type.
     */
    IProviderIncomingEventHandler getProviderIncomingEventHandler(ProviderIncomingEventType type);
}