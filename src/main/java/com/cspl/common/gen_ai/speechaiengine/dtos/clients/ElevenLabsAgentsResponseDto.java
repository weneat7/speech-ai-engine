package com.cspl.common.gen_ai.speechaiengine.dtos.clients;



import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElevenLabsAgentsResponseDto {

    @JsonProperty("agents")
    private List<AgentResponseDto> agents;

    @JsonProperty("next_cursor")
    private String nextCursor;

    @JsonProperty("has_more")
    private Boolean hasMore;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AgentResponseDto{

        @JsonProperty("agent_id")
        private String agentId;

        @JsonProperty("name")
        private String name;

        @JsonProperty("created_at_unix_secs")
        private long createdAtUnixSecs;

        @JsonProperty("access_info")
        private AccessInfo accessInfo;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Accessors(chain = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AccessInfo{

        @JsonProperty("is_creator")
        private boolean isCreator;

        @JsonProperty("creator_name")
        private String creatorName;

        @JsonProperty("creator_email")
        private String creatorEmail;

        @JsonProperty("role")
        private String role;
    }
}
