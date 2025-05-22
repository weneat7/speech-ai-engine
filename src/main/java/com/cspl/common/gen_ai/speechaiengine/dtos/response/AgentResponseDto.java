package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;

/**
 * DTO for {@link com.cspl.common.gen_ai.speechaiengine.models.entities}
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AgentResponseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @JsonProperty("agent_id")
    private String agentId;

    @JsonProperty("name")
    private String agentName;

    @JsonProperty("conversation_config")
    private ConversationConfig conversationConfig;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConversationConfig {
        private Agent agent;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Agent {
        public DynamicVariables dynamic_variables;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DynamicVariables {
        public Map<String, String> dynamic_variable_placeholders;
    }
}