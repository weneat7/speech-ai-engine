package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.config.RestClientHelper;
import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Transcription;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.services.IAfterCallService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@AllArgsConstructor
public class ElevenLabsAfterCallService implements IAfterCallService {

    private final RestClientHelper restClientHelper;

    private final AppProperties.ElevenLabsConfig elevenLabsConfig;

    private final CallRecordLogRepository callRecordLogRepository;

    private final ObjectMapper objectMapper;

    public AIProvider getProvider() {
        return AIProvider.ELEVEN_LABS;
    }

    @Override
    public void handleCallEndStatus(CallRecordLog callRecordLog) {
        Map<String,Object> response = restClientHelper.call(elevenLabsConfig.getUrl()+ APIConstants.CONVERSATIONS +"/"+callRecordLog.getConversationId(), Map.of(AppConstants.XI_API_KEY, elevenLabsConfig.getApiKey()));
        List<Map<String,Object>> list = objectMapper.convertValue(response.get(AppConstants.TRANSCRIPT), new TypeReference<List<Map<String, Object>>>() {});
        List<Transcription> transcriptions = new ArrayList<>();
        for (Map<String, Object> transcript : list) {
            transcriptions.add(
                    Transcription
                            .builder()
                            .content((String) transcript.getOrDefault(AppConstants.MESSAGE,null))
                            .role((String) transcript.get(AppConstants.ROLE))
                            .contentType(Objects.isNull(transcript.get(AppConstants.MESSAGE)) ? AppConstants.TEXT : AppConstants.ACTIONS)
                            .metaData(Map.of(
                                    AppConstants.TOOL_CALLS,transcript.getOrDefault(AppConstants.TOOL_CALLS,new ArrayList<>()),
                                    AppConstants.TOOL_RESULTS,transcript.getOrDefault(AppConstants.TOOL_RESULTS,new ArrayList<>()),
                                    AppConstants.FEEDBACK,transcript.getOrDefault(AppConstants.FEEDBACK,"")!=  null ? transcript.get("feedback") : ""
                            ))
                            .contentTime(transcript.get("time_in_call_secs") != null
                                    ? ((Number) transcript.get("time_in_call_secs")).longValue()
                                    : 0L)
                            .build()
            );
        }
        callRecordLog.setConversationResult(response);
        callRecordLog.setTranscriptions(transcriptions);
    }
}