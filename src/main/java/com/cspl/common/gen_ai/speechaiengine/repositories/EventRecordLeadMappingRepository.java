package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecordLeadMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


public interface EventRecordLeadMappingRepository extends MongoRepository<EventRecordLeadMapping,String> {

    Page<EventRecordLeadMapping> findByCampaignId(String campaignId, Pageable pageable);

    Optional<EventRecordLeadMapping> findByEventRecord_Id(String eventRecordId);
}
