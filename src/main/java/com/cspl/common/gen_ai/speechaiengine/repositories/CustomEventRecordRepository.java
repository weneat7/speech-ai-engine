package com.cspl.common.gen_ai.speechaiengine.repositories;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomEventRecordRepository {
    public List<EventRecord> findAndUpdateEventRecordByEventStatusInAndDateBetweenStartDate_EndDateAndTimeBetweenDailyStartTime_DailyStopTime(List<EventStatus> eventStatuses, EventStatus eventStatus, LocalDateTime dateTime);
}
