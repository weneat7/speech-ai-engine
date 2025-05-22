package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.IProviderSender;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.ProviderOutgoingMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Handler for AI media events
 */
@Service
@AllArgsConstructor
public class AIMediaEventHandler implements IAIIncomingEventHandler {

    /**
     * ProviderSender instance to send events to the provider
     */
    private final IProviderSender providerSender;

    /**
     * Get the type of the event handler
     * @return
     */
    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.MEDIA;
    }

    /**
     * Handle the incoming event
     * @param aiIncomingEvent
     * @param aiSession
     * @throws JsonProcessingException
     */
    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws JsonProcessingException {
        AIMediaEvent aiMediaEvent = (AIMediaEvent) aiIncomingEvent;
        Provider provider = SessionDetailsLogUtils.sessionDetailDtoMap.get(SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog().getProvider();
        String streamSid = SessionDetailsLogUtils.sessionDetailDtoMap.get(SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog().getStreamId();
        providerSender.sendToSession(ProviderOutgoingMediaEvent.builder().mediaPayload(aiMediaEvent.getMediaPayload()).streamId(streamSid).build(),
                SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()), provider);
    }
}
