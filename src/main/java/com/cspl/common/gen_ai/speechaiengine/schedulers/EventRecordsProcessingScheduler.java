package com.cspl.common.gen_ai.speechaiengine.schedulers;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.listeners.EventRecordListenerService;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordService;
import com.cspl.common.gen_ai.speechaiengine.utils.RedisServiceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class EventRecordsProcessingScheduler {

    /**
     * Repository for managing event records.
     */
    private final EventRecordRepository eventRecordRepository;

    /**
     * Redis service manager for managing Redis operations.
     */
    private final RedisServiceManager redisServiceManager;

    /**
     * Service for processing event records.
     */
    private final IEventRecordService eventRecordService;

    /**
     * Service for listening to event records.
     */
    private final EventRecordListenerService eventRecordListenerService;

    /**
     * Properties for event record process scheduler.
     */
    private final AppProperties.EventRecordProcessSchedulerProperties eventRecordProcessSchedulerProperties;

    /**
     * Asynchronous method to process event records.
     */
    @Async
    public void processEventRecordsAsync() {
        processEventRecords();
    }

    /**
     * Scheduled method to process event records.
     * This method is triggered every second.
     */
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    public void processEventRecords() {
        boolean acquired = redisServiceManager.setKeyIfAbsent("event-record-process-scheduler-key", "event-record-process-scheduler-value", 3, ChronoUnit.MINUTES);
        if (acquired) {
            // Double-checking the lock status
            String lockValue = (String) redisServiceManager.getValue("event-record-process-scheduler-key");
            if ("event-record-process-scheduler-value".equals(lockValue)) {
                log.info("[EventRecordsProcessingScheduler] Lock successfully acquired, executing cron job.");
                try {
                    List<EventStatus> allowedStatusList = eventRecordProcessSchedulerProperties.getAllowedStatusList();
                    List<EventRecord> eventRecords = eventRecordService.findAndUpdateEventRecord(allowedStatusList,EventStatus.QUEUED, LocalDateTime.now(ZoneOffset.UTC));
                    log.info("Event records to be processed: {}", eventRecords.size());
                    eventRecordService.processEventRecords(eventRecords, null);
                } catch (Exception e) {
                    log.error("[EventRecordsProcessingScheduler (59)] Error while processing event records: {}, {}", e.getMessage(), e.getStackTrace());
                }finally {
                    // Release the lock after job completion
                    redisServiceManager.delete("event-record-process-scheduler-key");
                    log.info("Cron job execution completed, releasing lock.");
                }
            }
        }else {
            log.info("[EventRecordsProcessingScheduler] Lock already acquired by another process, skipping execution.");
        }
    }

    @Scheduled(fixedDelay = 1000 * 5)
    public void fetchEventRecords() throws JsonProcessingException {
        int eventCounts = Integer.parseInt(redisServiceManager.setKeyIfAbsent(AppConstants.ONGOING_CONCURRENT_CALLS_SIZE,0));
        if(eventCounts < 5){
            eventRecordListenerService.pullMessages(5-eventCounts);
        }
    }
}