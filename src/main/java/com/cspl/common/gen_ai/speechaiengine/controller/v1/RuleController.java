package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.cspl.common.gen_ai.speechaiengine.services.IRuleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(AppConstants.API + AppConstants.V1 + APIConstants.RULE)
@AllArgsConstructor
public class RuleController {

    private IRuleService ruleService;

    @PostMapping
    @CrossOrigin("*")
    public Map<String, Object> createRule(@RequestBody RuleCreationDTO ruleCreationDTO) throws JsonProcessingException {
        return ruleService.createRule(ruleCreationDTO);
    }

    @GetMapping
    @CrossOrigin("*")
    public RuleCreationDTO getRule(@RequestParam("rule_id") String ruleId,@RequestHeader Map<String,String> headers){
        if(!headers.containsKey(AppConstants.TENANT_ID)) ResponseEntity.badRequest();
        return ruleService.getRule(ruleId,headers.get(AppConstants.TENANT_ID));
    }

    @GetMapping("/list")
    @CrossOrigin("*")
    public List<RuleCreationDTO> getAllRule(@RequestParam("page") String page,@RequestParam("size") String size){
        return ruleService.getAllRule(page,size);
    }
}
