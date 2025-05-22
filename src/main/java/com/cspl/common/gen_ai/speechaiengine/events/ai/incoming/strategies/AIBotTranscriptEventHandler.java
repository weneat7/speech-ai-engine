package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIBotTranscriptEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.SessionDetailsLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Transcription;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Handler for bot transcript events
 *
 */
@Service
@AllArgsConstructor
public class AIBotTranscriptEventHandler implements IAIIncomingEventHandler {

    private final CallRecordLogRepository callRecordLogRepository;
    /**
     * Get the type of the event handler
     * @return
     */
    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.BOT_TRANSCRIPT;
    }

    /**
     *
     * @param aiIncomingEvent
     * @param aiSession
     */
    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws Exception {
        AIBotTranscriptEvent aiBotTranscriptEvent = (AIBotTranscriptEvent) aiIncomingEvent;

        SessionDetailsLog sessionDetailsLog = SessionDetailsLogUtils.sessionDetailDtoMap.get(
                SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog();

        Transcription transcription = Transcription
                .builder()
                .role(AppConstants.AGENT)
                .content(aiBotTranscriptEvent.getBotTranscript())
                .contentType(AppConstants.TEXT)
                .contentTime(Duration.between(sessionDetailsLog.getSessionStartTime(), LocalDateTime.now()).getSeconds())
                .build();

        callRecordLogRepository.addTranscriptionToCallRecordLog(sessionDetailsLog.getCallSid(), transcription);
    }

}
