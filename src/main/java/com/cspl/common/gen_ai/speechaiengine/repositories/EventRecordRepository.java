package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRecordRepository extends MongoRepository<EventRecord,String>,CustomEventRecordRepository {

}
