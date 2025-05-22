package com.cspl.common.gen_ai.speechaiengine.config;

import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author: vineet.rajput
 * @use: This class is used to read the properties from the application.properties file
 **/

@Data
@Component
public class AppProperties {

    private AppProperties(){}
    /**
     * Plivo provider config.
     */
    @Data
    @ConfigurationProperties(prefix = "plivo.config")
    @Component
    public static class PlivoConfig {
        @NotNull
        private String answerUrl;
        @NotNull
        private String hangupUrl;
        @NotNull
        private String audioFormat;
        @NotNull
        private String wsUrl;
        @NotNull
        private String keepAlive;
        @NotNull
        private String bidirectional;
        @NotNull
        private String callUri;
        @NotNull
        private String authToken;
    }

    /**
     * Twilio provider config.
     */
    @Data
    @ConfigurationProperties(prefix = "twilio.config")
    @Component
    public static class TwilioConfig {
        @NotNull
        private String authToken;
        @NotNull
        private String answerUrl;
        @NotNull
        private String hangupUrl;
        @NotNull
        private String audioFormat;
        @NotNull
        private String wsUrl;
        @NotNull
        private String callUri;
        @NotNull
        private String recordingUri;
        @NotNull
        private String callDetailUri;
    }

    /**
     * Event Record Subscriber config.
     */
    @Data
    @ConfigurationProperties(prefix = "event.record.subscriber.config")
    @Component
    public static class EventRecordSubscriberConfig {
        @NotNull
        private String subscriberName;
    }

    /**
     * GCP config.
     */
    @Data
    @ConfigurationProperties(prefix = "gcp.pubsub.config")
    @Component
    public static class GCPPubSubConfig {
        @NotNull
        private String keyPath;
        @NotNull
        private String projectId;
    }


    /**
     * Event Record Publisher config
     */
    @Data
    @ConfigurationProperties(prefix = "event.record.publisher.config")
    @Component
    public static class EventRecordPublisherConfig {
        @NotNull
        private String topicName;
    }
    @Data
    @ConfigurationProperties(prefix = "gcp.storage")
    @Component
    public static class GCPStorageProperties {
        @NotNull
        private String baseUrl;

        @NotNull
        private String bucketName;

        @NotNull
        private String projectId;

        @NotNull
        private String keyFilePath;
    }
    @ConfigurationProperties(prefix = "eleven-labs")
    @Component
    @Data
    public static class ElevenLabsConfig {
        @NotNull
        private String url;

        @NotNull
        private String apiKey;
    }

    /**
     * The type SAE header config.
     */
    @Data
    @ConfigurationProperties(prefix = "sae-header-config")
    @Component
    public static class SAEHeaderConfig {

        /**
         * The Auth api key.
         */
        private String authApiKey;

        /**
         * The Config api key.
         */
        private String configApiKey;

        /**
         * The Cache api key.
         */
        private String cacheApiKey;
    }

    @Data
    @ConfigurationProperties(prefix = "event-record-process-scheduler")
    @Component
    public static class EventRecordProcessSchedulerProperties {

        /**
         * The Event record process scheduler properties.
         */
        private List<EventStatus> allowedStatusList;
    }

    @Data
    @ConfigurationProperties(prefix = "rule.config")
    @Component
    public static class RuleEngineProperties {
        private String createRuleUri;
        private String fetchRuleUri;
        private String authToken;
        private String source;
        private String fetchAllRuleUri;
        private String fetchRuleByIds;
    }

    @Data
    @Component
    @ConfigurationProperties(prefix = "auth")
    public class AuthProperties {
        private List<String> publicPaths;
    }
}
