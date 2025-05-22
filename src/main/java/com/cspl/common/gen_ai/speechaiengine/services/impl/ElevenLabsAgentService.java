package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dtos.clients.ElevenLabsAgentsResponseDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.AgentResponseDto;
import com.cspl.common.gen_ai.speechaiengine.services.IAgentService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to fetch agents from ElevenLabs
 * @see IAgentService
 * @see AgentResponseDto
 * @see ElevenLabsAgentsResponseDto
 * @see AppProperties.ElevenLabsConfig
 *
 */
@Service("elevenLabsAgentService")
@AllArgsConstructor
@Slf4j
public class ElevenLabsAgentService implements IAgentService {

    /**
     * RestTemplate to make HTTP calls
     */
    private final RestTemplate restTemplate;

    /**
     * Configuration for ElevenLabs
     */
    private final AppProperties.ElevenLabsConfig elevenLabsConfig;

    /**
     * Fetch all agents from ElevenLabs
     * @param pageSize
     * @return
     */
    @Override
    public List<AgentResponseDto> getAllAgents(Integer pageSize) throws Exception {
        WebClient webClient = WebClient.builder()
                .baseUrl(elevenLabsConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(AppConstants.XI_API_KEY, elevenLabsConfig.getApiKey())
                .build();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/agents")
                .queryParam("page_size", pageSize);
        ElevenLabsAgentsResponseDto response = webClient.get()
                .uri(uriBuilder.toUriString())
                .retrieve()
                .bodyToMono(ElevenLabsAgentsResponseDto.class)
                .block();
        if (response != null) {
            return getAgentResponseDtos(response);
        }
        throw new Exception("Failed to fetch agents from ElevenLabs");
    }


    /**
     * Convert ElevenLabs response to AgentResponseDto
     * @param response
     * @return
     */
    private List<AgentResponseDto> getAgentResponseDtos(ElevenLabsAgentsResponseDto response) {
        List<ElevenLabsAgentsResponseDto.AgentResponseDto> responseList =  response.getAgents();
        List<AgentResponseDto> agentResponseList = new ArrayList<>();
        for(ElevenLabsAgentsResponseDto.AgentResponseDto agentResponseDto : responseList) {
            AgentResponseDto agentResponse = new AgentResponseDto();
            agentResponse.setAgentId(agentResponseDto.getAgentId());
            agentResponse.setAgentName(agentResponseDto.getName());
            agentResponseList.add(agentResponse);
        }
        return agentResponseList;
    }


    /**
     * Fetch agent by Id
     * @param agentId
     * @return
     */
    @Override
    @Cacheable(value = "elevenlabs:agentById", key = "#agentId")
    public AgentResponseDto getAgentById(String agentId) {
        WebClient webClient = WebClient.builder()
                .baseUrl(elevenLabsConfig.getUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(AppConstants.XI_API_KEY, elevenLabsConfig.getApiKey())
                .build();

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString("/agents/"+agentId);

        return  webClient.get()
                .uri(uriBuilder.toUriString())
                .retrieve()
                .bodyToMono(AgentResponseDto.class)
                .block();
    }
}
