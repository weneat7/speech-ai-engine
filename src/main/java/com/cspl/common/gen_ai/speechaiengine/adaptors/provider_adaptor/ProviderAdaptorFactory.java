package com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Factory class to get the ProviderAdaptor based on the Provider
 */
@Service
@AllArgsConstructor
public class ProviderAdaptorFactory implements IProviderAdaptorFactory{

    /**
     * Map to hold the ProviderAdaptor instances
     */
    private final Map<String,IProviderAdaptor> providerAdaptorMap;

    /**
     * Method to get the ProviderAdaptor based on the Provider
     * @param provider
     * @return
     */
    public IProviderAdaptor getProviderAdaptor(Provider provider){
        return providerAdaptorMap.get(provider.toString().toLowerCase()+"Adaptor");
    }
}