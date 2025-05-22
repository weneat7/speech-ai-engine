package com.cspl.common.gen_ai.speechaiengine;

import com.github.sonus21.rqueue.spring.EnableRqueue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableWebFlux
@SpringBootApplication(
        exclude = {
                org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration.class
        }
)
@EnableMongoAuditing
@EnableScheduling
@EnableCaching
@EnableRqueue
public class SpeechAiEngineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpeechAiEngineApplication.class, args);
    }

    @Bean
    ServletWebServerFactory servletWebServerFactory() {
        return new TomcatServletWebServerFactory();
    }

}