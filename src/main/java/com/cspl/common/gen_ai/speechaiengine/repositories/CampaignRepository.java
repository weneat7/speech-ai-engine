package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends MongoRepository<Campaign,String>, CustomRepository<String,Campaign> {
    List<Campaign> findAllByCampaignStatus(CampaignStatus campaignStatus);
}