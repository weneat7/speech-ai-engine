package com.cspl.common.gen_ai.speechaiengine.models.enums;

public class Retry {

    /**
     * Enum for retry trigger reasons
     */
    public enum Status {
        ELIGIBLE,           // Can be retried
        IN_PROGRESS,        // Currently being retried
        EXHAUSTED,          // Max attempts reached
        PAUSED,             // Temporarily paused
        COMPLETED,          // Successfully completed
        CANCELLED           // Manually cancelled
    }

    /**
     * Enum for retry strategy types
     */
    public enum Strategy {
        LINEAR,
        EXPONENTIAL
    }

    /**
     * Enum for retry trigger reasons
     */
    public enum Trigger {
        CALL_FAILED,        // Technical failure
        BUSY_SIGNAL,        // Customer busy
        NO_ANSWER,          // Customer didn't answer
        NETWORK_ERROR,      // Network issues
        TIMEOUT,            // Call timeout
        PROVIDER_ERROR      // Telephony provider error
    }
}
