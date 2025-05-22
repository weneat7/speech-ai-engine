package com.cspl.common.gen_ai.speechaiengine.services.selectors;

import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;

import java.util.Optional;

public interface ICallingServiceFactory {
    public Optional<ICallingService> getCallingService(DialerType provider);
}
