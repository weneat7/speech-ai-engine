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
 * The TwilioAdaptor class implements the IProviderAdaptor interface
 */
@Slf4j
@AllArgsConstructor
@Service("twilioAdaptor")
public class TwilioAdaptor implements IProviderAdaptor{

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
    public Optional<IProviderIncomingEvent> convertToCommonProviderEvent(String message) throws JsonProcessingException {
        Map<String,Object> messageMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        String event = messageMap.get(AppConstants.EVENT).toString();
        switch (event){
            case AppConstants.START -> {
                String callSid = objectMapper.convertValue(messageMap.get(AppConstants.START), new TypeReference<Map<String,Object>>() {}).get(AppConstants.TWILIO_CALL_SID).toString();
                return Optional.of(ProviderStartEvent
                        .builder()
                        .callSid(callSid)
                        .streamSid(messageMap.get(AppConstants.TWILIO_STREAM_SID).toString())
                        .provider(Provider.TWILIO)
                        .build());
            }
            case AppConstants.MEDIA -> {
                String mediaPayload = objectMapper.convertValue(messageMap.get(AppConstants.MEDIA), new TypeReference<Map<String, Object>>() {}).get(AppConstants.PAYLOAD).toString();
                return Optional.of(
                        ProviderMediaEvent.builder()
                                .streamSid(messageMap.get(AppConstants.TWILIO_STREAM_SID).toString())
                                .mediaPayload(mediaPayload)
                                .provider(Provider.TWILIO)
                                .build()
                );
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
                ProviderOutgoingMediaEvent providerOutgoingMediaEvent = (ProviderOutgoingMediaEvent) providerOutgoingEvent;
                ObjectNode mediaNode = objectMapper.createObjectNode();
                mediaNode.put(AppConstants.PAYLOAD, providerOutgoingMediaEvent.getMediaPayload());

                ObjectNode rootNode = objectMapper.createObjectNode();
                rootNode.put(AppConstants.EVENT, AppConstants.MEDIA);
                rootNode.set( AppConstants.MEDIA, mediaNode);
                rootNode.put(AppConstants.TWILIO_STREAM_SID, providerOutgoingMediaEvent.getStreamId());
                return rootNode.toString();
            }
            case INTERRUPT -> {
                ObjectNode mediaNode = objectMapper.createObjectNode();
                mediaNode.put(AppConstants.EVENT, "clear");
                mediaNode.put(AppConstants.TWILIO_STREAM_SID,((ProviderOutgoingInterruptEvent) providerOutgoingEvent).getStreamSid());
                return mediaNode.toString();
            }
        }
        return "";
    }
}
