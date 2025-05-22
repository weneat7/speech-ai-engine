package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderIncomingEventType;
import org.springframework.web.reactive.socket.WebSocketSession;

public interface IProviderIncomingEventHandler {

    public ProviderIncomingEventType getType();

    public void handleProviderIncomingEvent(IProviderIncomingEvent event, WebSocketSession providerSession) throws Exception;
}
