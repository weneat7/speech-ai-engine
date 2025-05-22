package com.cspl.common.gen_ai.speechaiengine.publishers;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IPublisher {
    public void publish(Object callEventRecordRequestDTO) throws JsonProcessingException;
}
