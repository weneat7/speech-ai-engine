package com.cspl.common.gen_ai.speechaiengine.config;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.stub.GrpcSubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStub;
import com.google.cloud.pubsub.v1.stub.SubscriberStubSettings;
import com.google.pubsub.v1.TopicName;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Configuration
@AllArgsConstructor
public class PubSubConfig {

    private final AppProperties.EventRecordPublisherConfig eventRecordPublisherConfig;

    private final AppProperties.GCPPubSubConfig gcpPubSubConfig;

    @Bean
    public SubscriberStub subscriberStub() throws IOException {

        byte[] serviceKeyByteArray = Base64.getDecoder().decode(gcpPubSubConfig.getKeyPath());
        InputStream inputStream = new ByteArrayInputStream(serviceKeyByteArray);
        GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(inputStream);

        SubscriberStubSettings settings = SubscriberStubSettings.newBuilder()
                .setCredentialsProvider(() -> credentials)
                .build();

        return GrpcSubscriberStub.create(settings);
    }

    @Bean(name = "eventRecordPublisher")
    public Publisher eventRecordPublisher() throws IOException {
        TopicName topicName = TopicName.of(gcpPubSubConfig.getProjectId(), eventRecordPublisherConfig.getTopicName());

        byte[] serviceKeyByteArray = Base64.getDecoder().decode(gcpPubSubConfig.getKeyPath());
        InputStream inputStream = new ByteArrayInputStream(serviceKeyByteArray);
        GoogleCredentials credentials = ServiceAccountCredentials
                .fromStream(inputStream);

        return Publisher
                .newBuilder(topicName)
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .setEnableCompression(false)
                .build();
    }
}
