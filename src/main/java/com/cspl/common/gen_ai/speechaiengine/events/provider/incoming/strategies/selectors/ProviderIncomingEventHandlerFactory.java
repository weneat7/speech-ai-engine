package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies.selectors;

import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies.IProviderIncomingEventHandler;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Factory class for creating instances of IProviderIncomingEventHandler.
 * This class is responsible for managing a list of event handlers and providing the appropriate handler based on the event type.
 */
@Service
@RequiredArgsConstructor
public class ProviderIncomingEventHandlerFactory implements IProviderIncomingEventHandlerFactory{

    private final List<IProviderIncomingEventHandler> providerIncomingEventHandlerList;

    private Map<ProviderIncomingEventType,IProviderIncomingEventHandler> providerIncomingEventHandlerMap = new HashMap<>();

    @Override
    public IProviderIncomingEventHandler getProviderIncomingEventHandler(ProviderIncomingEventType type) {
        return providerIncomingEventHandlerMap.get(type);
    }

    @PostConstruct
    public void init(){
        for(IProviderIncomingEventHandler providerIncomingEventHandler:providerIncomingEventHandlerList){
            Assert.notNull(providerIncomingEventHandler.getType(),"Type cannot be Null");
            providerIncomingEventHandlerMap.put(providerIncomingEventHandler.getType(),providerIncomingEventHandler);
        }
    }
}
