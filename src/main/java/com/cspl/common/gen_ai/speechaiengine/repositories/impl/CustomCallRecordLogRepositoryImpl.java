package com.cspl.common.gen_ai.speechaiengine.repositories.impl;

import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Transcription;
import com.cspl.common.gen_ai.speechaiengine.repositories.CustomCallRecordLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class CustomCallRecordLogRepositoryImpl implements CustomCallRecordLogRepository {
    private final MongoTemplate mongoTemplate;

    public void addTranscriptionToCallRecordLog(String callSid, Transcription transcription) {
        Query query = new Query(Criteria.where("callSid").is(callSid));
        Update update = new Update().push("transcriptions", transcription);

        mongoTemplate.updateFirst(query, update, CallRecordLog.class);
    }

    public void addConversationIdToCallRecordLog(String callSid, String conversationId) {
        Query query = new Query(Criteria.where("callSid").is(callSid));
        Update update = new Update().set("conversationId", conversationId);

        mongoTemplate.updateFirst(query, update, CallRecordLog.class);
    }
}
