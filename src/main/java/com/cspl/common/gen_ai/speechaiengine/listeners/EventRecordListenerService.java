package com.cspl.common.gen_ai.speechaiengine.listeners;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.events.application.PullEvent;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.EventRecordRepository;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordValidationService;
import com.cspl.common.gen_ai.speechaiengine.services.selectors.ICallingServiceFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.pubsub.v1.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service responsible for pulling messages from a Google Pub/Sub subscription
 * and initiating call flow events based on the message content.
 */
@Service
@Slf4j
@AllArgsConstructor
public class EventRecordListenerService {

    private final SubscriberStub subscriberStub;

    private final AppProperties.EventRecordSubscriberConfig eventRecordSubscriberConfig;

    private final ObjectMapper objectMapper;

    private final ICallingServiceFactory callingServiceFactory;

    private final IEventRecordValidationService eventRecordValidationService;

    private final EventRecordRepository eventRecordRepository;

    /**
     * Pulls messages from the configured Pub/Sub subscription and processes them.
     *
     * @throws JsonProcessingException if message deserialization fails
     */
    public void pullMessages(int n) throws JsonProcessingException {
        PullRequest pullRequest = PullRequest.newBuilder()
                .setMaxMessages(n)
                .setReturnImmediately(false)
                .setSubscription(eventRecordSubscriberConfig.getSubscriberName())
                .build();

        PullResponse pullResponse = subscriberStub.pullCallable().call(pullRequest);
        List<ReceivedMessage> messages = pullResponse.getReceivedMessagesList();

        for (ReceivedMessage message : messages) {
            try {
                log.info("Received message: {}", message.getMessage().getData().toStringUtf8());

                EventRecord eventRecord = objectMapper.readValue(message.getMessage().getData().toStringUtf8(), EventRecord.class);

                if(eventRecordValidationService.validateEventRecordCallable(eventRecord)){
                    callingServiceFactory.getCallingService(eventRecord.getDialerType()).ifPresent(callingService -> {
                        try {
                            callingService.initiateCallFlowEvent(eventRecord);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
                else if(!eventRecordValidationService.validateEventStatus(eventRecord.getId())){
                    eventRecord.setEventStatus(EventStatus.PAUSED);
                    eventRecordRepository.save(eventRecord);
                }
            }
            finally {
                AcknowledgeRequest ackRequest = AcknowledgeRequest.newBuilder()
                        .setSubscription(eventRecordSubscriberConfig.getSubscriberName())
                        .addAckIds(message.getAckId())
                        .build();
                subscriberStub.acknowledgeCallable().call(ackRequest);
            }
        }
    }

    @EventListener
    public void pullMessage(PullEvent pullEvent) throws JsonProcessingException {
        pullMessages(1);
    }
}
