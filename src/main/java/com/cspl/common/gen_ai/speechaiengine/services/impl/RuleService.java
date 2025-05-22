package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.config.RestClientHelper;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.cspl.common.gen_ai.speechaiengine.services.IRuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RuleService implements IRuleService {

    private final RestClientHelper restClientHelper;
    private final AppProperties.RuleEngineProperties ruleEngineProperties;
    private final ObjectMapper objectMapper;

    @Override
    public Map<String ,Object> createRule(RuleCreationDTO ruleCreationDTO) throws JsonProcessingException {
        ruleCreationDTO.setRuleId(UUID.randomUUID().toString());
        ruleCreationDTO.setSource(ruleEngineProperties.getSource());
        return restClientHelper
                .call(
                        ruleEngineProperties.getCreateRuleUri(),
                        ruleCreationDTO,
                        Map.of("Authorization",ruleEngineProperties.getAuthToken()),
                        MediaType.APPLICATION_JSON
                );
    }

    @Override
    public RuleCreationDTO getRule(String ruleId,String tenantID){
        return objectMapper.convertValue(restClientHelper
                .call(
                        ruleEngineProperties.getFetchRuleUri()+ruleId,
                        Map.of("Authorization",ruleEngineProperties.getAuthToken(), AppConstants.TENANT_ID,tenantID)
                ).get("data"),RuleCreationDTO.class);
    }

    @Override
    public List<RuleCreationDTO> getAllRule(String page,String size){
        return objectMapper.convertValue(restClientHelper.call(
                ruleEngineProperties.getFetchAllRuleUri()+"?page="+page+"&"+size,
                Map.of("Authorization", ruleEngineProperties.getAuthToken(), AppConstants.TENANT_ID, "speech_ai_engine")
        ).get("data"), new TypeReference<List<RuleCreationDTO>>() {});
    }

    @Override
    public List<RuleCreationDTO> getAllRuleByIds(List<String> ruleIds) throws JsonProcessingException {
        Map<String,Object> ruleIdsListDTO = new HashMap<>();
        ruleIdsListDTO.put("rule_ids",ruleIds);
        return objectMapper.convertValue(
                restClientHelper.call(
                        ruleEngineProperties.getFetchRuleByIds(),
                        ruleIdsListDTO,
                        Map.of("Authorization", ruleEngineProperties.getAuthToken(), AppConstants.TENANT_ID, "speech_ai_engine"),
                        MediaType.APPLICATION_JSON
                ).get("data"), new TypeReference<List<RuleCreationDTO>>() {});
    }


}
