package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;

public interface IEventRecordValidationService {
    public boolean validateEventRecordCallable(EventRecord eventRecord);

    public boolean validateEventRecordCreation(EventRecord eventRecord);

    public boolean validateEventStatus(String id);
}
