package com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.dto.SessionDetailsDTO;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.AIOutgoingMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.cspl.common.gen_ai.speechaiengine.webSocketHandlers.ElevenLabsWebSocketHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Handler for the MEDIA event from the provider.
 */
@Service
@AllArgsConstructor
public class ProviderMediaEventHandler implements IProviderIncomingEventHandler{

    /**
     * WebSocket handler for Eleven Labs.
     */
    private final ElevenLabsWebSocketHandler aiWebSocketHandler;

    /**
     * Get the type of the event.
     * @return The type of the event.
     */
    @Override
    public ProviderIncomingEventType getType() {
        return ProviderIncomingEventType.MEDIA;
    }

    /**
     * Handle the incoming event from the provider.
     * @param event
     * @param providerSession
     * @throws JsonProcessingException
     */
    @Override
    public void handleProviderIncomingEvent(IProviderIncomingEvent event, WebSocketSession providerSession) throws JsonProcessingException {
        if(SessionDetailsLogUtils.sessionDetailDtoMap.containsKey(providerSession.getId())) {
            ProviderMediaEvent providerMediaEvent = (ProviderMediaEvent) event;
            SessionDetailsDTO sessionDetailsLog = SessionDetailsLogUtils.sessionDetailDtoMap.get(providerSession.getId());
            if(sessionDetailsLog.getAiSessionsMap().containsKey(AIProvider.ELEVEN_LABS)) {
                aiWebSocketHandler.sendMessage(sessionDetailsLog.getAiSessionsMap().get(AIProvider.ELEVEN_LABS), AIOutgoingMediaEvent // get from agent in sessionDetailsDTO
                        .builder()
                        .mediaPayload(providerMediaEvent.getMediaPayload())
                        .build(), AIProvider.ELEVEN_LABS);
            }
        }
    }
}
