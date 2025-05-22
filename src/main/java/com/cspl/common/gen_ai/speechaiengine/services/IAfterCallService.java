package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.CallStatus;

public interface IAfterCallService {
    public void handleCallEndStatus(CallRecordLog callRecordLog, CallStatus callStatus);
}
