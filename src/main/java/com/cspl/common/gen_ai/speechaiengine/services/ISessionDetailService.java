package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import org.springframework.web.reactive.socket.WebSocketSession;

/**
 * Service interface for managing session details.
 */
public interface ISessionDetailService {

    /**
     * Creates a new session detail record.
     * @param callSid
     * @param streamId
     * @param providerSession
     */
    public void processSessionDetailsLogCreation(String callSid, String streamId, WebSocketSession providerSession, Provider provider) throws Exception;
}
