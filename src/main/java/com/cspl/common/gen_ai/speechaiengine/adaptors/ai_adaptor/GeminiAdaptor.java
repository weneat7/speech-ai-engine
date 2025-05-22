package com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.AIOutgoingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.AIOutgoingMediaEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.outgoing.IAIOutgoingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service("GEMINI_ADAPTOR")
public class GeminiAdaptor implements IAIAdaptor{

    private final ObjectMapper objectMapper;

    @Override
    public String getSpecificAIOutGoingMessage(IAIOutgoingEvent aiOutgoingEvent) throws JsonProcessingException {

        AIOutgoingEventType type = aiOutgoingEvent.getType();

        if (type == AIOutgoingEventType.MEDIA) {
            AIOutgoingMediaEvent mediaEvent = (AIOutgoingMediaEvent) aiOutgoingEvent;
            String mediaPayload = mediaEvent.getMediaPayload();

            ObjectNode root = objectMapper.createObjectNode();
            ObjectNode realtimeInput = objectMapper.createObjectNode();
            ArrayNode mediaChunks = objectMapper.createArrayNode();
            ObjectNode mediaChunk = objectMapper.createObjectNode();

            mediaChunk.put("mime_type", "audio/pcm");
            mediaChunk.put("data", mediaPayload);
            mediaChunks.add(mediaChunk);

            realtimeInput.set("media_chunks", mediaChunks);
            root.set("realtime_input", realtimeInput);
            return root.toString();
        }

        return "";
    }

    @Override
    public Optional<IAIIncomingEvent> getCommonAIEvent(String message) throws JsonProcessingException {
        Map<String,Object> messageMap = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
        if(messageMap.containsKey("serverContent")){
            Map<String,Object> serverMessage = objectMapper.readValue(messageMap.get("serverContent").toString(),new TypeReference<Map<String, Object>>() {});
            if(serverMessage.containsKey("modelTurn")){
                String mediaPayload = objectMapper.convertValue(serverMessage, JsonNode.class).get("parts").get(0).get("inlineData").get("data").asText();
                return Optional.of(AIMediaEvent.builder().mediaPayload(mediaPayload).build());
            }
        }
        return Optional.empty();
    }

    @Override
    public String getInitialMessage(EventRecord eventRecord) {
        return "{\n" +
                "  \"setup\": {\n" +
                "    \"model\": \"models/gemini-2.0-flash-exp\",\n" +
                "    \"system_instruction\": {\n" +
                "        role:\"user\",\n" +
                "        parts:[\n" +
                "            {\n" +
                "                text:\"You are a customer executive at Cars24. Your job is to talk to customers over the call and check if they will be available at the time they booked for their car inspection. You can casually speak in Hinglish.\n" +
                "\n" +
                "Here are the customer details you need to use for the conversation:\n" +
                "\n" +
                "Name: {{customer_name}}\n" +
                "\n" +
                "Car: {{make}} {{model}} {{year}} {{variant}}\n" +
                "\n" +
                "Appointment Details: {{inspection_date}} {{inspection_slot}}\n" +
                "\n" +
                "Address: {{inspection_address}}\n" +
                "\n" +
                "Appointment Token: {{appointment_token}} \n" +
                "\n" +
                "Inspection Type: {{inspection_type}}\n" +
                "\n" +
                "If the customer is not available at the booked time, you need to ask them when they will be available for inspection and reschedule their appointment. Take the details from them and record the new date and slot they prefer. You have a tool to fetch available slots, so make sure to check the slots before suggesting them to the customer. Do not overwhelm user with suggestion with all slots at once. Do turn by turn conversation with maximum 3 slots in one time.  Make sure you are fetching slots from the tool before suggesting\n" +
                "\n" +
                "The customer can also change their address if needed. In this case, you will suggest slots based on the new address. Do not ask for user's address upfront, unless user asks for address change.\n" +
                "\n" +
                "Do not confirm address unless users asks for address change.\n" +
                "\n" +
                "Appointment token is internal  to our system, user should never ask for it.\n" +
                "\n" +
                "Important: Do not accept any slot other than the ones available in the system. If the customer suggests a different time, politely let them know that no other slots are available right now. Request them to cancel their current slot using the app if they can’t make it.\n" +
                "\n" +
                "Keep the conversation natural and friendly. If something is unclear, don’t hesitate to ask the customer again. You can always say there is some disturbance in line due to which customer's response is not clear.  Always end the call with proper greetings – the call cannot end randomly.\n" +
                "\n" +
                "Always respond in English vocabulary. \"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "  }\n" +
                "}\n";
    }
}
