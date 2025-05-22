package com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.IAIOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Optional;

/**
 * Interface for AI Adaptor
 */
public interface IAIAdaptor {

    /**
     *
     * @param aiOutgoingEvent
     * @return
     * @throws JsonProcessingException
     */
    public String getSpecificAIOutGoingMessage(IAIOutgoingEvent aiOutgoingEvent) throws JsonProcessingException;

    /**
     *
     * @param message
     * @return
     * @throws JsonProcessingException
     */
    public Optional<IAIIncomingEvent> getCommonAIEvent(String message) throws JsonProcessingException;

    public String getInitialMessage(EventRecord eventRecord);
}
