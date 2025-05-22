package com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor;

import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.IProviderOutgoingEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

/**
 * Interface for Provider Adaptor
 */
public interface IProviderAdaptor {

    /**
     *
     * @param message
     * @return
     * @throws JsonProcessingException
     */
    public Optional<IProviderIncomingEvent> convertToCommonProviderEvent(String message) throws JsonProcessingException;

    /**
     *
     * @param providerOutgoingEvent
     * @return
     * @throws JsonProcessingException
     */
    public String convertToSpecificEvent(IProviderOutgoingEvent providerOutgoingEvent) throws JsonProcessingException;
}