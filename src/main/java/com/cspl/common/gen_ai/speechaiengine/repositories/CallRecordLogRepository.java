package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CallRecordLogRepository extends MongoRepository<CallRecordLog,String> , CustomCallRecordLogRepository {
    Optional<CallRecordLog> findByCallSid(String callSid);
}
