package com.cspl.common.gen_ai.speechaiengine.repositories.impl;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.CustomEventRecordRepository;
import lombok.AllArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomEventRecordRepositoryImpl implements CustomEventRecordRepository {

    MongoTemplate mongoTemplate;

    /**
     * to update event status to new status by fetching event records currently in given zone
     * @param eventStatuses
     * @param eventStatus
     * @return
     */
    public List<EventRecord> findAndUpdateEventRecordByEventStatusInAndDateBetweenStartDate_EndDateAndTimeBetweenDailyStartTime_DailyStopTime(List<EventStatus> eventStatusList, EventStatus eventStatus, LocalDateTime localDateTime)
    {
        Document expr1 = new Document(AppConstants.D_AND, List.of(
                new Document(AppConstants.D_GREATER_THAN, List.of(AppConstants.D_DAILY_START_TIME, AppConstants.D_DAILY_STOP_TIME)),
                new Document(AppConstants.D_EXPR, new Document(AppConstants.D_OR, List.of(
                        new Document(AppConstants.D_GREATER_THAN, List.of(AppConstants.D_DAILY_START_TIME, localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")))),
                        new Document(AppConstants.D_LESSER_THAN, List.of(AppConstants.D_DAILY_STOP_TIME, localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))))
                )))
        ));

        Document expr2 = new Document(AppConstants.D_AND, List.of(
                new Document(AppConstants.D_LESSER_THAN, List.of(AppConstants.D_DAILY_START_TIME, AppConstants.D_DAILY_STOP_TIME)),
                new Document(AppConstants.D_LESSER_THAN, List.of(AppConstants.D_DAILY_START_TIME, localDateTime.format(DateTimeFormatter.ofPattern("HH:mm")))),
                new Document(AppConstants.D_GREATER_THAN, List.of(AppConstants.D_DAILY_STOP_TIME, localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))))
        ));

        Criteria criteria = new Criteria().andOperator(
                Criteria.where(AppConstants.START_DATE_TIME).lte(localDateTime),
                Criteria.where(AppConstants.END_DATE_TIME).gt(localDateTime),
                Criteria.where(AppConstants.EVENT_STATUS).in(eventStatusList),
                new Criteria().orOperator(
                        Criteria.where(AppConstants.D_EXPR).is(expr1),
                        Criteria.where(AppConstants.D_EXPR).is(expr2)
                )
        );
        Query query = new Query(criteria);
        List<EventRecord> eventRecords = mongoTemplate.find(query, EventRecord.class);
        List<String> eventRecordIds = eventRecords.stream().map(EventRecord::getId).toList();

        Update update = new Update().set("eventStatus", eventStatus);
        mongoTemplate.updateMulti(query, update, EventRecord.class);

        return mongoTemplate.find(Query.query(Criteria.where("_id").in(eventRecordIds)), EventRecord.class);
    }
}
