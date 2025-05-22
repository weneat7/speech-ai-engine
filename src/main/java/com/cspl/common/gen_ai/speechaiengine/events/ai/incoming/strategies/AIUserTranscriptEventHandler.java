package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIUserTranscriptEvent;
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
 * Handler for user transcript events
 */
@Service
@AllArgsConstructor
public class AIUserTranscriptEventHandler implements IAIIncomingEventHandler {

    private final CallRecordLogRepository callRecordLogRepository;
    /**
     * Get the type of the event handler
     * @return
     */
    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.USER_TRANSCRIPT;
    }

    /**
     *  Extract the user and transcript from the event and save it to the database
     * @param aiIncomingEvent
     * @param aiSession
     */
    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws Exception {
        AIUserTranscriptEvent aiUserTranscriptEvent = (AIUserTranscriptEvent) aiIncomingEvent;

        SessionDetailsLog sessionDetailsLog = SessionDetailsLogUtils.sessionDetailDtoMap.get(
                SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog();

        Transcription transcription = Transcription
                .builder()
                .role(AppConstants.USER)
                .content(aiUserTranscriptEvent.getUserTranscript())
                .contentType(AppConstants.TEXT)
                .contentTime(Duration.between(sessionDetailsLog.getSessionStartTime(), LocalDateTime.now()).getSeconds())
                .build();

        callRecordLogRepository.addTranscriptionToCallRecordLog(sessionDetailsLog.getCallSid(), transcription);
    }
}
