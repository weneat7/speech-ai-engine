package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.adaptors.ai_adaptor.factory.IAIAdaptorFactory;
import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.dto.SessionDetailsDTO;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.SessionDetailsLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.ISessionDetailService;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import com.cspl.common.gen_ai.speechaiengine.webSocketHandlers.ElevenLabsWebSocketHandler;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@AllArgsConstructor
public class SessionDetailsLogService implements ISessionDetailService {

    private final ElevenLabsWebSocketHandler aiWebSocketHandler;
    private final CallRecordLogRepository callRecordLogRepository;
    private final EventRecordRepository eventRecordRepository;
    private final IAIAdaptorFactory aiAdaptorFactory;
    private final AppProperties.ElevenLabsConfig elevenLabsConfig;
    private final ElevenLabsAgentService elevenLabsAgentService;
    /**
     * to start ai provider connection and save session details
     * @param callSid
     * @param streamId
     * @param providerSession
     * @throws Exception
     */
    public void processSessionDetailsLogCreation(String callSid, String streamId, WebSocketSession providerSession, Provider provider) throws Exception {

        SessionDetailsLog sessionDetailsLog = SessionDetailsLog
                .builder()
                .sessionId(providerSession.getId())
                .streamId(streamId)
                .callSid(callSid)
                .provider(provider)
                .sessionStartTime(LocalDateTime.now())
                .build();

        SessionDetailsLogUtils
                .sessionDetailDtoMap.put(providerSession.getId(),
                        SessionDetailsDTO
                        .builder()
                                .aiSessionsMap(new HashMap<>())
                        .sessionDetailsLog(sessionDetailsLog).build());

        CallRecordLog callRecordLog = callRecordLogRepository.findByCallSid(callSid).orElseThrow(()->new RuntimeException("call record not found"));

        EventRecord eventRecord = eventRecordRepository.findById(callRecordLog.getEventKey()).orElseThrow(()->new RuntimeException("no eventRecord found"));

        aiWebSocketHandler.connect(
                elevenLabsConfig.getUrl()+"/conversation?agent_id="+eventRecord.getAgentId(),
                providerSession,
                aiAdaptorFactory.getAIAdaptor(AIProvider.ELEVEN_LABS).getInitialMessage(eventRecord)
                , AIProvider.ELEVEN_LABS).subscribe();

//        aiWebSocketHandler.connect("wss://generativelanguage.googleapis.com/ws/google.ai.generativelanguage.v1beta.GenerativeService.BidiGenerateContent?key=AIzaSyBKQwTPzFvhdIHnuIFePaRcoO3Cw1GtiHQ",
//                providerSession,
//                aiAdaptorFactory.getAIAdaptor(AIProvider.GEMINI).getInitialMessage(eventRecord)
//                , AIProvider.GEMINI).subscribe();
    }

}
