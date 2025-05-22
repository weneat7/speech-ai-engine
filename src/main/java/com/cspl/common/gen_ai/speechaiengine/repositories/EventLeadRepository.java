package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventLeadRepository extends MongoRepository<EventLead,String> {
}
