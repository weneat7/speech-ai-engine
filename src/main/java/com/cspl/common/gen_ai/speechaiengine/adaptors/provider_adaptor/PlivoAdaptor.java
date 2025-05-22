package com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.IProviderIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.incoming.ProviderStartEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.IProviderOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.ProviderOutgoingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.ProviderOutgoingInterruptEvent;
import com.cspl.common.gen_ai.speechaiengine.events.provider.outgoing.ProviderOutgoingMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * The PlivoAdaptor class implements the IProviderAdaptor interface
 */
@Service("plivoAdaptor")
@AllArgsConstructor
@Slf4j
public class PlivoAdaptor implements IProviderAdaptor {

    /**
     * ObjectMapper instance for JSON processing
     */
    private final ObjectMapper objectMapper;

    /**
     * Method to convert the incoming message to a common provider event
     * @param message
     * @return
     * @throws JsonProcessingException
     */
    @Override
    public Optional<IProviderIncomingEvent> convertToCommonProviderEvent(String message) throws JsonProcessingException {
        Map<String,Object> messageMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        String event = messageMap.get("event").toString();
        switch (event){
            case "start" -> {
                Map<String, Object> eventConfigs = (Map<String,Object>)messageMap.get(AppConstants.START);
                String callSid = eventConfigs.get(AppConstants.PLIVO_CALL_SID).toString();
                String streamSid = eventConfigs.get(AppConstants.PLIVO_STREAM_ID).toString();
                return Optional.of(ProviderStartEvent.builder().callSid(callSid).streamSid(streamSid).provider(Provider.PLIVO).build());
            }
            case "media" -> {
                String mediaPayload = ((Map<String,Object>)messageMap.get(AppConstants.MEDIA)).get(AppConstants.PAYLOAD).toString();
                return Optional.of(ProviderMediaEvent.builder().mediaPayload(mediaPayload).provider(Provider.PLIVO).build());
            }
        }
        return Optional.empty();
    }


    /**
     * Method to convert the provider outgoing event to a specific event
     * @param providerOutgoingEvent
     * @return
     * @throws JsonProcessingException
     */
    public String convertToSpecificEvent(IProviderOutgoingEvent providerOutgoingEvent) throws JsonProcessingException {
        ProviderOutgoingEventType providerOutgoingEventType = providerOutgoingEvent.getType();
        switch (providerOutgoingEventType){
            case MEDIA -> {
                ProviderOutgoingMediaEvent providerMediaEvent = (ProviderOutgoingMediaEvent) providerOutgoingEvent;
                ObjectNode mediaNode = objectMapper.createObjectNode();
                mediaNode.put("contentType", "audio/x-mulaw");
                mediaNode.put("sampleRate", 8000);
                mediaNode.put("payload", providerMediaEvent.getMediaPayload());
                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put("event", "playAudio");
                rootNode.set("media", mediaNode);
                return objectMapper.writeValueAsString(rootNode);
            }
            case INTERRUPT -> {
                ProviderOutgoingInterruptEvent providerOutgoingInterruptEvent = (ProviderOutgoingInterruptEvent) providerOutgoingEvent;
                ObjectNode mediaNode = objectMapper.createObjectNode();
                mediaNode.put("event", "clearAudio");
                mediaNode.put("streamId",providerOutgoingInterruptEvent.getStreamSid());
                return objectMapper.writeValueAsString(mediaNode);
            }
        }
        return "";
    }
}
