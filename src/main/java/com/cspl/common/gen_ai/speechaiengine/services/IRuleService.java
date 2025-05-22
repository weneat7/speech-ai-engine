package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;
import java.util.Map;

public interface IRuleService {
    public Map<String,Object> createRule(RuleCreationDTO ruleCreationDTO) throws JsonProcessingException;
    public RuleCreationDTO getRule(String ruleId,String tenantID);
    public List<RuleCreationDTO> getAllRule(String page,String size);
    public List<RuleCreationDTO> getAllRuleByIds(List<String> ruleIds) throws JsonProcessingException;
}
