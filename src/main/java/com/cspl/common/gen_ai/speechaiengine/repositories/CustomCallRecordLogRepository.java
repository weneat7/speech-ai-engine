package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Transcription;

public interface CustomCallRecordLogRepository {
    public void addTranscriptionToCallRecordLog(String callSid, Transcription transcription);
    public void addConversationIdToCallRecordLog(String callSid, String conversationId);
}
