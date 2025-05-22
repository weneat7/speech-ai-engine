package com.cspl.common.gen_ai.speechaiengine.utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface IRedisServiceManager {
    public Object getValue(String key);
    public void set(String key, Object object, long timeout, TimeUnit timeUnit);
    public void set(String key, Object object);
    public void delete(String keyPattern);
    public Set<String> getAllKeys();
    public boolean isKeyPresent(String key);
}
