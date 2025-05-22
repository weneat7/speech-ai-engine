package com.cspl.common.gen_ai.speechaiengine.adaptors.provider_adaptor;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;

/**
 * Factory interface to get the ProviderAdaptor based on the Provider
 */
public interface IProviderAdaptorFactory {

    /**
     *
     * @param provider
     * @return
     */
    public IProviderAdaptor getProviderAdaptor(Provider provider);
}
