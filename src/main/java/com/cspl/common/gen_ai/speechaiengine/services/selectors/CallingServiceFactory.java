package com.cspl.common.gen_ai.speechaiengine.services.selectors;

import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.services.ICallingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CallingServiceFactory implements ICallingServiceFactory {

    private final Map<String, ICallingService> callingServiceMap;

    public Optional<ICallingService> getCallingService(DialerType provider){
        switch (provider){
            case TWILIO -> {return Optional.of(callingServiceMap.get("twilioCallingService"));}
            case PLIVO -> {return Optional.of(callingServiceMap.get("plivoCallingService"));}
        }
        return Optional.empty();
    }


}
