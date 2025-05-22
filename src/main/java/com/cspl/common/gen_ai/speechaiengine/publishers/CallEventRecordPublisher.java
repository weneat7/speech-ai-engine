package com.cspl.common.gen_ai.speechaiengine.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutureCallback;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CallEventRecordPublisher implements IPublisher{

    private final Publisher publisher;
    private final ObjectMapper objectMapper;

    public CallEventRecordPublisher(@Qualifier("eventRecordPublisher") Publisher publisher, ObjectMapper objectMapper) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
    }

    public void publish(Object eventRecordRequestDTO) throws JsonProcessingException {
        ByteString data = ByteString.copyFromUtf8(objectMapper.writeValueAsString(eventRecordRequestDTO));
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

        ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);

        ApiFutures.addCallback(
                messageIdFuture,
                new ApiFutureCallback<>() {
                    @Override
                    public void onSuccess(String messageId) {
                        log.info("Message published successfully. Message ID: " + messageId);
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        log.error("Error publishing message: " + throwable.getMessage());
                    }
                },
                MoreExecutors.directExecutor()
        );
    }
}
