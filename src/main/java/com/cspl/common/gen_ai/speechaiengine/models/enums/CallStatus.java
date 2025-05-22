package com.cspl.common.gen_ai.speechaiengine.models.enums;

import lombok.Getter;

@Getter
public enum CallStatus {
    IN_PROGRESS("in-progress"),
    COMPLETED("completed"),
    FAILED("failed"),
    BUSY("busy"),
    NO_ANSWER("no-answer");

    private final String value;

    CallStatus(String value) {
        this.value = value;
    }

    public static CallStatus getFromValue(String value) throws Exception {
        for (CallStatus callStatus : CallStatus.values()) {
            if (callStatus.getValue().equalsIgnoreCase(value)) {
                return callStatus;
            }
        }
        throw  new Exception("");
    }
}
