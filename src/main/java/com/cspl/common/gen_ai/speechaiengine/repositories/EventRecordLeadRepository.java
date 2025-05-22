package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecordLeadMapping;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface EventRecordLeadRepository extends MongoRepository<EventRecordLeadMapping, String> {

    Optional<EventRecordLeadMapping> findByEventRecord_Id(String eventRecordId);
}
