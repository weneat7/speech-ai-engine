package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.AgentResponseDto;
import com.cspl.common.gen_ai.speechaiengine.services.IAgentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author vineet.rajput
 * Controller class for Agent
 * This class will handle all the requests related to Agent
 */
@RestController
@RequestMapping( APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.AGENT)
@AllArgsConstructor
public class AgentController {

    /**
     * The eleven labs agent service.
     */
    private final IAgentService elevenLabsAgentService;

    /**
     * Get all agents response entity.
     *
     * @param pageSize the page size
     * @return @link ResponseEntity<AgentResponseDto>
     */
    @GetMapping("/all")
    @CrossOrigin("*")
    public ResponseEntity<List<AgentResponseDto>> getAllAgents(@RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize) throws Exception {
        List<AgentResponseDto> agents = elevenLabsAgentService.getAllAgents(pageSize);
        return ResponseEntity.ok().body(agents);
    }

    /**
     * Get agent by id response entity.
     *
     * @param agentId the agent id
     * @return the response entity<AgentResponseDto>
     */
    @GetMapping("/{agentId}")
    @CrossOrigin("*")
    public ResponseEntity<AgentResponseDto> getAgentById(@PathVariable String agentId){
        AgentResponseDto agent = elevenLabsAgentService.getAgentById(agentId);
        return ResponseEntity.ok().body(agent);
    }
}
