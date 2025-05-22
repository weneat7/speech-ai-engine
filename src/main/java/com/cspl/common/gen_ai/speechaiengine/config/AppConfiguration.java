package com.cspl.common.gen_ai.speechaiengine.config;


import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.sonus21.rqueue.config.SimpleRqueueListenerContainerFactory;
import com.github.sonus21.rqueue.utils.Constants;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.socket.client.ReactorNettyWebSocketClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.http.client.WebsocketClientSpec;

import java.text.SimpleDateFormat;

/**
 * @author vineet.rajput
 * @use: This class is used to read the properties from the application.properties file
 * The type App configuration.
 */
@Configuration
public class AppConfiguration {

    /**
     * Ant path matcher ant path matcher.
     *
     * @return the ant path matcher
     */
    @Bean
    public AntPathMatcher antPathMatcher(){return new AntPathMatcher();}

    /**
     * Rest template rest template.
     *
     * @return the rest template
     */
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplateBuilder().build();
    }

    /**
     * Message converter message converter.
     *
     * @return the message converter
     */
    @Bean
    public MessageConverter messageConverter() {return new MappingJackson2MessageConverter();}

    /**
     * Object mapper object mapper.
     *
     * @return the object mapper
     */
    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = JsonMapper.builder()
                .findAndAddModules()
                .build();
        objectMapper.setDateFormat(new SimpleDateFormat(AppConstants.DATE_FORMAT));
        return objectMapper;
    }

    @Bean
    public MappingMongoConverter mappingMongoConverter(MongoDatabaseFactory mongoDatabaseFactory, MongoMappingContext mongoMappingContext) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
        converter.setTypeMapper(new DefaultMongoTypeMapper(null)); // Removes _class field
        converter.setMapKeyDotReplacement("~"); // Replace dots in map keys with ~
        return converter;
    }


    /**
     * The type Thread pool properties.
     */
    @Data
    @ConfigurationProperties(prefix = "thread-pool")
    @Component
    public static class ThreadPoolProperties {
        /**
         * The Core pool size.
         */
        private int corePoolSize = 5;
        /**
         * The Max pool size.
         */
        private int maxPoolSize = 10;
        /**
         * The Queue capacity.
         */
        private int queueCapacity = 25;
        /**
         * The Keep alive in sec.
         */
        private int keepAliveInSec = 60;
        /**
         * The Name.
         */
        private String name = "SBE-THREAD-Pool-";
    }

    /**
     * Web socket client reactor netty web socket client.
     * @return RectorNettyWebSocketClient
     */
    @Bean
    public ReactorNettyWebSocketClient webSocketClient() {
        return new ReactorNettyWebSocketClient(
                HttpClient.create(),()-> WebsocketClientSpec.builder()
                .maxFramePayloadLength(10 * 1024 * 1024)
        );
    }

    /**
     * Rest client bean.
     * @return RestClient
     */
    @Bean
    public RestClient restClient(){
        return RestClient.builder().build();
    }

    /**
     * the message resource for the given error code
     * @return MessageSource
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/message");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public MeterRegistry meterRegistry() {
        return new SimpleMeterRegistry();
    }

    /**
     * Simple rqueue listener container factory simple rqueue listener container factory.
     *
     * @return the simple rqueue listener container factory
     */
    @Bean
    public SimpleRqueueListenerContainerFactory simpleRqueueListenerContainerFactory() {
        SimpleRqueueListenerContainerFactory simpleRqueueListenerContainerFactory =
                new SimpleRqueueListenerContainerFactory();
        simpleRqueueListenerContainerFactory.setMaxNumWorkers(5);
        simpleRqueueListenerContainerFactory.setPollingInterval(Constants.ONE_MILLI);
        return simpleRqueueListenerContainerFactory;
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("My Reactive API")
                        .version("1.0"));
    }
}
