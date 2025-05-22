package com.cspl.common.gen_ai.speechaiengine.events.application;

import org.springframework.context.ApplicationEvent;

public class PullEvent extends ApplicationEvent {

    public PullEvent(Object source) {
        super(source);
    }
}
