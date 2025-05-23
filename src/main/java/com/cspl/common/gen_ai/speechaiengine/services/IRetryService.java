package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;

import java.time.LocalDateTime;
import java.util.List;

public interface IRetryService {

    void processCallFailure(String eventRecordId, String campaignId, CallStatus callStatus, String failureReason);

    List<EventRecord> getEventsReadyForRetry(int limit);

    void scheduleRetry(EventRecord eventRecord, String campaignId, LocalDateTime retryTime, Retry.Trigger trigger);

    boolean isReadyForRetry(EventRecord eventRecord);

    void resetDailyCounters();

    void resetWeeklyCounters();

    void resetMonthlyCounters();
}
