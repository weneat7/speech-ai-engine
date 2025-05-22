package com.cspl.common.gen_ai.speechaiengine.repositories.impl;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.repositories.CustomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CustomRepositoryImpl implements CustomRepository<String,Campaign>{

    private final MongoTemplate mongoTemplate;

    @Override
    public void markEntityAsDeleted(String entityId, Campaign entityName) {
        Query query = new Query(Criteria.where("_id").is(entityId));
        Update update = new Update().set("deleted",true);
        mongoTemplate.updateMulti(query,update, entityName.getClass());
    }

}
