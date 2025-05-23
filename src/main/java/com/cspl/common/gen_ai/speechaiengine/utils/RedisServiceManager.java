package com.cspl.common.gen_ai.speechaiengine.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author priyanshu.gupta
 * <p>
 * The class Redis service manager.
 */
@Slf4j
@Component
public class RedisServiceManager implements IRedisServiceManager {

    /**
     * The Redis template.
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    /**
     * The Redis.
     */
    private ValueOperations<String, Object> redis;

    /**
     * Init.
     */
    @PostConstruct
    public void init() {
        redis = redisTemplate.opsForValue();
    }

    /**
     * Gets hash value.
     *
     * @param key the key
     * @return the hash value
     */
    @Override
    public Object getValue(String key) {
        return redis.get(key);
    }

    /**
     * @param key     the key
     * @param object  the object
     * @param timeout the timeout in hour
     * @use: set the key and corresponding value in cache
     */
    @Override
    public void set(String key, Object object, long timeout, TimeUnit timeUnit) {
        redis.set(key, object, timeout, timeUnit);
    }

    /**
     * Delete.
     *
     * @param keyPattern the key pattern
     */
    @Override
    public void deletePattern(String keyPattern) {
        redisTemplate.keys(keyPattern)
                .forEach(key -> redisTemplate.delete(key));
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Gets all keys.
     *
     * @return the all keys
     */
    @Override
    public Set<String> getAllKeys() {
        return redisTemplate.keys("*");
    }


    @Override
    public void set(String key, Object object) {
        redis.set(key, object);
    }

    /**
     *
     * @use: set the key and corresponding value in cache
     * @param key     the key
     * @param object  the object
     * @param timeout the timeout in hour
     */
    public void set(String key, Object object,long timeout) {
        redis.set(key, object,timeout, TimeUnit.HOURS);
    }


    /**
     * @use: set the key and corresponding value in cache
     * @param key
     * @param value
     * @param timeout
     * @param timeUnit
     * @return
     */
    public boolean setKeyIfAbsent(String key, String value, long timeout, ChronoUnit timeUnit) {
        return Boolean.TRUE.equals(redisTemplate.opsForValue().setIfAbsent( key, value, Duration.of(timeout, timeUnit)));
    }

    public String setKeyIfAbsent(String key,Object value){
        redisTemplate.opsForValue().setIfAbsent(key,value);
        return getValue(key).toString();
    }

    public boolean isKeyPresent(String key){
        return redisTemplate.hasKey(key);
    }
}


