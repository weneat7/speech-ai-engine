package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordValidationService;
import com.cspl.common.gen_ai.speechaiengine.utils.IRedisServiceManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.xml.bind.ValidationException;
import java.time.*;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
@Slf4j
public class EventRecordValidationService implements IEventRecordValidationService {

    private final ElevenLabsAgentService elevenLabsAgentService;
    private final IRedisServiceManager redisServiceManager;

    @Override
    public boolean validateEventRecordCallable(EventRecord eventRecord){
        try{
        return validateDateTime(eventRecord.getStartDateTime(), eventRecord.getEndDateTime()) &&
                validateDynamicVariablesPresent(eventRecord) &&
                validateEventStatus(eventRecord.getId());
        }catch (Exception e){
            log.error("Error while validating event record : {} with message: {}", eventRecord.getId(), e.getMessage());
        }
        return false;
    }

    @Override
    public boolean validateEventRecordCreation(EventRecord eventRecord) {
        try {
            return validateDynamicVariablesPresent(eventRecord);
        }catch (Exception e){
            log.error("Error while validating event record : {} with message: {}", eventRecord.getId(), e.getMessage());
        }
        return false;
    }

    public boolean validateTime(String startTime,String endTime){
        if(Objects.nonNull(startTime)&&Objects.nonNull(endTime)) {
            LocalTime startLocalTime = LocalTime.parse(startTime);
            LocalTime endLocalTime = LocalTime.parse(endTime);

            return (LocalTime.now(ZoneOffset.UTC).isAfter(startLocalTime) || LocalTime.now(ZoneOffset.UTC).equals(startLocalTime)) && (LocalTime.now(ZoneOffset.UTC).isBefore(endLocalTime) || LocalTime.now(ZoneOffset.UTC).equals(endLocalTime));
        }
        else{
            return true;
        }
    }

    public boolean validateDate(String startDate,String endDate){
        if(Objects.nonNull(startDate)&&Objects.nonNull(endDate)) {
            LocalDate startLocalDate = LocalDate.parse(startDate);
            LocalDate endLocalDate = LocalDate.parse(endDate);

            return (LocalDate.now(ZoneOffset.UTC).isAfter(startLocalDate) || LocalDate.now(ZoneOffset.UTC).isEqual(startLocalDate)) && (LocalDate.now(ZoneOffset.UTC).isBefore(endLocalDate) || LocalDate.now(ZoneOffset.UTC).isEqual(endLocalDate));
        }
        else {
            return true;
        }
    }

    public boolean validateDynamicVariablesPresent(EventRecord eventRecord) throws ValidationException {
        Map<String,String> dynamicVars = elevenLabsAgentService.getAgentById(eventRecord.getAgentId()).getConversationConfig()
                .getAgent().getDynamic_variables().getDynamic_variable_placeholders();
        Map<String,Object> requestData = eventRecord.getRequestData();
        for(String key:dynamicVars.keySet()){
            if(!requestData.containsKey(key))
                throw new ValidationException("Dynamic variable "+key+" is not present in request data");
        }
        return true;
    }

    public boolean validateEventStatus(String id) {
        if(Objects.nonNull(id) && redisServiceManager.isKeyPresent(id))
            return redisServiceManager.getValue(id).equals(CampaignStatus.ACTIVE.toString());
        else return true;
    }

    public boolean validateDateTime(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        if(Objects.isNull(startDateTime) || Objects.isNull(endDateTime)) {
            return false;
        }
        LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
        return (now.isAfter(startDateTime) || now.isEqual(startDateTime))
                && (now.isBefore(endDateTime) || now.isEqual(endDateTime));
    }
}