package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.dtos.response.AgentResponseDto;

import java.util.List;

/**
 * Service interface for managing agents.
 */
public interface IAgentService {

    /**
     * Creates a new agent.
     * @param pageSize
     * @return
     * @throws Exception
     */
    public List<AgentResponseDto> getAllAgents(Integer pageSize) throws Exception;

    /**
     * Fetches an agent by its ID.
     * @param agentId
     * @return
     */
    public AgentResponseDto getAgentById(String agentId);

}
