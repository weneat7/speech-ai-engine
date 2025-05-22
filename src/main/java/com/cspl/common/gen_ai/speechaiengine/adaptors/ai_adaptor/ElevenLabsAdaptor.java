package com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.AgentResponseDto;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.*;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.AIOutgoingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.AIOutgoingMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.IAIOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.services.impl.ElevenLabsAgentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@AllArgsConstructor
@Service("ELEVEN_LABS_ADAPTOR")
public class ElevenLabsAdaptor implements IAIAdaptor {

    private final ObjectMapper objectMapper;

    private final ElevenLabsAgentService elevenLabsAgentService;

    /**
     * Method to get the AIIncomingEvent based on the message
     * @param message
     * @return
     * @throws JsonProcessingException
     */
    public Optional<IAIIncomingEvent> getCommonAIEvent(String message) throws JsonProcessingException {
        Map<String,Object> messageMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        String event = messageMap.get(AppConstants.TYPE).toString();
            switch (event) {
                case "audio" -> {
                    String mediaPayload = objectMapper.valueToTree(messageMap.get("audio_event"))
                            .path("audio_base_64").asText();
                    return Optional.of(
                        AIMediaEvent.builder().mediaPayload(mediaPayload).build()
                );}
                case "agent_response" -> {
                    String transcript = objectMapper.valueToTree(messageMap.get("agent_response_event")).path("agent_response").toString();
                    return Optional.of(
                            AIBotTranscriptEvent.builder().botTranscript(transcript).build()
                    );
                }
                case "interruption" -> {return Optional.of(AIInterruptEvent.builder().build());}
                case "user_transcript" -> {
                    String userTranscript = objectMapper.valueToTree(messageMap.get("user_transcription_event"))
                            .path("user_transcript").asText();
                    return Optional.of(
                        AIUserTranscriptEvent.builder()
                                .userTranscript(userTranscript)
                                .build()
                );}
                case "conversation_initiation_metadata" -> {
                    String conversationId = objectMapper.valueToTree(messageMap.get("conversation_initiation_metadata_event")).path("conversation_id").asText();
                    return Optional.of(
                            AIInitiationEvent.builder()
                                    .conversationId(conversationId)
                                    .build()
                    );
                }
            }
        return Optional.empty();
    }

    /**
     *
     * @param aiOutgoingEvent
     * @return
     * @throws JsonProcessingException
     */
    public String getSpecificAIOutGoingMessage(IAIOutgoingEvent aiOutgoingEvent) throws JsonProcessingException {

        AIOutgoingEventType type = aiOutgoingEvent.getType();
        switch (type){
            case MEDIA -> {
                AIOutgoingMediaEvent mediaEvent = (AIOutgoingMediaEvent) aiOutgoingEvent;
                String mediaPayload = mediaEvent.getMediaPayload();
                ObjectNode userMediaNode = objectMapper.createObjectNode();
                userMediaNode.put("user_audio_chunk",mediaPayload);
                return objectMapper.writeValueAsString(userMediaNode);
            }
        }
        return "";
    }

    public String getInitialMessage(EventRecord eventRecord){
        AgentResponseDto agentResponseDto = elevenLabsAgentService.getAgentById(eventRecord.getAgentId());
        Map<String,String> dynamic_variables = agentResponseDto.getConversationConfig().getAgent().getDynamic_variables().getDynamic_variable_placeholders();
        Map<String,Object> requestData =  eventRecord.getRequestData();

        if(Objects.nonNull(requestData)) {
            for (String key : requestData.keySet()) {
                dynamic_variables.put(key, requestData.get(key).toString());
            }
        }

        ObjectNode conversationInitiation = objectMapper.createObjectNode();
        ObjectNode dynamicVarNode = objectMapper.convertValue(dynamic_variables,ObjectNode.class);

        conversationInitiation.put("type","conversation_initiation_client_data");
        conversationInitiation.put("dynamic_variables",dynamicVarNode);

        return conversationInitiation.toString();
    }
}
