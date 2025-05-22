package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.strategies;

import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIIncomingEventType;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.AIInitiationEvent;
import com.cspl.common.gen_ai.speechaiengine.events.ai.incoming.IAIIncomingEvent;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.utils.SessionDetailsLogUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketSession;

@AllArgsConstructor
@Service
public class AIInitiationEventHandler implements IAIIncomingEventHandler {

    private final CallRecordLogRepository callRecordLogRepository;

    @Override
    public AIIncomingEventType getType() {
        return AIIncomingEventType.AI_INITIATION;
    }

    @Override
    public void handleEvent(IAIIncomingEvent aiIncomingEvent, WebSocketSession aiSession) throws Exception {
        AIInitiationEvent aiInitiationEvent = (AIInitiationEvent) aiIncomingEvent;

        callRecordLogRepository.addConversationIdToCallRecordLog(SessionDetailsLogUtils.sessionDetailDtoMap.get(
                SessionDetailsLogUtils.aiToProviderSessionMap.get(aiSession.getId()).getId()).getSessionDetailsLog().getCallSid(),
                aiInitiationEvent.getConversationId());
    }
}
