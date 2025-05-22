package com.cspl.common.gen_ai.speechaiengine.events.ai.incoming;

/**
 * Interface for all incoming events from the AI engine.
 */
public interface IAIIncomingEvent {

    /**
     * Get the type of the incoming event.
     * @return
     */
    public AIIncomingEventType getType();
}
