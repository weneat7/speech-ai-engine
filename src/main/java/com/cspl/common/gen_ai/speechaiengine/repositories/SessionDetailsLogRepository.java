package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.SessionDetailsLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionDetailsLogRepository extends MongoRepository<SessionDetailsLog,String> {
}
