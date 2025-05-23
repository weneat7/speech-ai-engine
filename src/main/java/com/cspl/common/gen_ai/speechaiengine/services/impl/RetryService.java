package com.cspl.common.gen_ai.speechaiengine.services.impl;

import com.cspl.common.gen_ai.speechaiengine.config.EventRecordRetryConfiguration;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.EventRecordMetadata;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.RetryAttempt;
import com.cspl.common.gen_ai.speechaiengine.dto.metadata.RetryMetadata;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.models.enums.Retry;
import com.cspl.common.gen_ai.speechaiengine.repositories.CampaignRepository;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.ICampaignService;
import com.cspl.common.gen_ai.speechaiengine.services.IRetryService;
import com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.IRetryStrategy;
import com.cspl.common.gen_ai.speechaiengine.services.strategies.retries.factory.RetryStrategyFactory;
import com.cspl.common.gen_ai.speechaiengine.utils.TimeWindowUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Core retry service that handles call failure processing and retry scheduling
 */
@Service
@AllArgsConstructor
@Slf4j
public class RetryService implements IRetryService {

    private final EventRecordRepository eventRecordRepository;
    private final CampaignRepository campaignRepository;
    private final RetryStrategyFactory retryStrategyFactory;
    private final ICampaignService campaignService;

    @Override
    @Async
    public void processCallFailure(String eventRecordId, String campaignId, CallStatus callStatus, String failureReason) {
        try {
            log.info("Processing call failure - EventRecord: {}, Campaign: {}, Status: {}",
                    eventRecordId, campaignId, callStatus);

            EventRecord eventRecord = eventRecordRepository.findById(eventRecordId)
                    .orElseThrow(() -> new RuntimeException("EventRecord not found: " + eventRecordId));

            Campaign campaign = campaignService.getCampaign(campaignId);

            // Get appropriate retry strategy
            IRetryStrategy retryStrategy = retryStrategyFactory.getStrategy(campaign);

            // Determine retry trigger
            Retry.Trigger  trigger = determineRetryTrigger(callStatus, campaign);

            // Record the failure attempt
            recordFailureAttempt(eventRecord, callStatus, trigger, failureReason);

            // Check if we should retry
            if (retryStrategy.shouldRetry(eventRecord, campaign, callStatus)) {
                // Calculate next retry time
                LocalDateTime nextRetryTime = retryStrategy.calculateNextRetryTime(eventRecord, campaign, callStatus, trigger);

                // Adjust to campaign time window if needed
                if (shouldRespectTimeWindows(campaign)) {
                    nextRetryTime = TimeWindowUtils.adjustToTimeWindow(nextRetryTime, campaign);
                }

                // Schedule the retry
                scheduleRetry(eventRecord, campaignId, nextRetryTime, trigger);

                log.info("Retry scheduled for EventRecord: {} at {}", eventRecordId, nextRetryTime);
            } else {
                // Mark as exhausted
                markAsExhausted(eventRecord, "Retry limits exceeded or non-retryable status");
                log.info("EventRecord: {} marked as exhausted - no more retries", eventRecordId);
            }

            // Save the updated event record
            eventRecordRepository.save(eventRecord);

        } catch (Exception e) {
            log.error("Error processing call failure for EventRecord: {}, Campaign: {}",
                    eventRecordId, campaignId, e);
        }
    }

    @Override
    public List<EventRecord> getEventsReadyForRetry(int limit) {
        LocalDateTime now = LocalDateTime.now();

        return eventRecordRepository.findAll()
                .stream()
                .filter(this::isEventReadyForRetry)
                .filter(eventRecord -> {
                    RetryMetadata retryMetadata = getRetryMetadata(eventRecord);
                    return retryMetadata.getNextRetryTime() != null &&
                            retryMetadata.getNextRetryTime().isBefore(now);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public void scheduleRetry(EventRecord eventRecord, String campaignId, LocalDateTime retryTime, Retry.Trigger trigger) {
        // Initialize metadata if needed
        ensureMetadataExists(eventRecord);

        RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();

        // Update retry metadata
        retryMetadata.setNextRetryTime(retryTime);
        retryMetadata.setStatus(Retry.Status.ELIGIBLE);

        // Update event record status
        eventRecord.setEventStatus(EventStatus.PENDING);

        log.debug("Scheduled retry for EventRecord: {} at {}", eventRecord.getId(), retryTime);
    }

    @Override
    public boolean isReadyForRetry(EventRecord eventRecord) {
        return isEventReadyForRetry(eventRecord);
    }

    @Override
    public void resetDailyCounters() {
        log.info("Resetting daily retry counters");

        List<EventRecord> eventsWithRetryData = eventRecordRepository.findAll()
                .stream()
                .filter(this::hasRetryMetadata)
                .collect(Collectors.toList());

        for (EventRecord eventRecord : eventsWithRetryData) {
            RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();
            retryMetadata.setDailyAttempts(0);
        }

        eventRecordRepository.saveAll(eventsWithRetryData);
        log.info("Reset daily counters for {} events", eventsWithRetryData.size());
    }

    @Override
    public void resetWeeklyCounters() {
        log.info("Resetting weekly retry counters");

        List<EventRecord> eventsWithRetryData = eventRecordRepository.findAll()
                .stream()
                .filter(this::hasRetryMetadata)
                .collect(Collectors.toList());

        for (EventRecord eventRecord : eventsWithRetryData) {
            RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();
            retryMetadata.setWeeklyAttempts(0);
        }

        eventRecordRepository.saveAll(eventsWithRetryData);
        log.info("Reset weekly counters for {} events", eventsWithRetryData.size());
    }

    @Override
    public void resetMonthlyCounters() {
        log.info("Resetting monthly retry counters");

        List<EventRecord> eventsWithRetryData = eventRecordRepository.findAll()
                .stream()
                .filter(this::hasRetryMetadata)
                .collect(Collectors.toList());

        for (EventRecord eventRecord : eventsWithRetryData) {
            RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();
            retryMetadata.setMonthlyAttempts(0);
        }

        eventRecordRepository.saveAll(eventsWithRetryData);
        log.info("Reset monthly counters for {} events", eventsWithRetryData.size());
    }

    // Private helper methods

    private void recordFailureAttempt(EventRecord eventRecord, CallStatus callStatus,
                                      Retry.Trigger trigger, String failureReason) {
        ensureMetadataExists(eventRecord);

        RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();

        // Create attempt record
        RetryAttempt attempt = RetryAttempt.builder()
                .attemptNumber(retryMetadata.getTotalAttempts() + 1)
                .attemptTime(LocalDateTime.now(ZoneOffset.UTC))
                .callStatus(callStatus)
                .trigger(trigger)
                .reason(failureReason)
                .build();

        // Add to history
        retryMetadata.getAttemptHistory().add(attempt);

        // Update counters
        retryMetadata.setTotalAttempts(retryMetadata.getTotalAttempts() + 1);
        retryMetadata.setDailyAttempts(retryMetadata.getDailyAttempts() + 1);
        retryMetadata.setWeeklyAttempts(retryMetadata.getWeeklyAttempts() + 1);
        retryMetadata.setMonthlyAttempts(retryMetadata.getMonthlyAttempts() + 1);

        // Update timing and failure info
        retryMetadata.setLastAttemptTime(LocalDateTime.now());
        retryMetadata.setLastFailureStatus(callStatus);
        retryMetadata.setLastTrigger(trigger);
        retryMetadata.setLastFailureReason(failureReason);

        if (retryMetadata.getFirstAttemptTime() == null) {
            retryMetadata.setFirstAttemptTime(LocalDateTime.now());
        }
    }

    private void markAsExhausted(EventRecord eventRecord, String reason) {
        ensureMetadataExists(eventRecord);

        RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();
        retryMetadata.setStatus(Retry.Status.EXHAUSTED);

        eventRecord.setEventStatus(EventStatus.FAILED);

        log.debug("Marked EventRecord: {} as exhausted: {}", eventRecord.getId(), reason);
    }

    private void ensureMetadataExists(EventRecord eventRecord) {
        if (eventRecord.getMetaData() == null) {
            eventRecord.setMetaData(EventRecordMetadata.builder().build());
        }

        if (eventRecord.getMetaData().getRetryMetadata() == null) {
            eventRecord.getMetaData().setRetryMetadata(RetryMetadata.builder().build());
        }

        if (eventRecord.getMetaData().getRetryMetadata().getAttemptHistory() == null) {
            eventRecord.getMetaData().getRetryMetadata().setAttemptHistory(new ArrayList<>());
        }
    }

    private boolean isEventReadyForRetry(EventRecord eventRecord) {
        if (eventRecord.getEventStatus() != EventStatus.PENDING) {
            return false;
        }

        if (!hasRetryMetadata(eventRecord)) {
            return false;
        }

        RetryMetadata retryMetadata = eventRecord.getMetaData().getRetryMetadata();
        return retryMetadata.getStatus() == Retry.Status.ELIGIBLE;
    }

    private boolean hasRetryMetadata(EventRecord eventRecord) {
        return eventRecord.getMetaData() != null &&
                eventRecord.getMetaData().getRetryMetadata() != null;
    }

    private RetryMetadata getRetryMetadata(EventRecord eventRecord) {
        if (!hasRetryMetadata(eventRecord)) {
            return RetryMetadata.builder().build();
        }
        return eventRecord.getMetaData().getRetryMetadata();
    }

    private Retry.Trigger determineRetryTrigger(CallStatus callStatus, Campaign campaign) {
        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);
        return config.getStatusToTriggerMapping().getOrDefault(callStatus, Retry.Trigger.CALL_FAILED);
    }

    private boolean shouldRespectTimeWindows(Campaign campaign) {
        EventRecordRetryConfiguration config = getRetryConfiguration(campaign);
        return config.getRespectTimeWindows() != null && config.getRespectTimeWindows();
    }

    private EventRecordRetryConfiguration getRetryConfiguration(Campaign campaign) {
        if (campaign.getMetadata().getEventRecordRetryConfiguration() != null) {
            return campaign.getMetadata().getEventRecordRetryConfiguration();
        }

        // Default configuration
        return EventRecordRetryConfiguration.builder()
                .enabled(true)
                .respectTimeWindows(true)
                .build();
    }
}