package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Service interface for managing call records and events.
 */
public interface ICallingService {

    /**
     * Creates a new call record.
     * @param callSid
     * @param status
     * @throws Exception
     */
    public void updateCallRecordStatus(String callSid, String status) throws Exception;

    /**
     * Fetches a call record by its SID.
     * @param callSid
     * @return
     */
    public void initiateCallFlowEvent(EventRecord eventRecord) throws Exception;

    /**
     * Fetches a call record by its SID.
     * @param callSid
     * @return
     */
    public String configureWebSocketConnection(String callSid) throws JsonProcessingException;

    /**
     * handle recording on call end
     * @param recordingUrl
     * @param callSid
     * @param recordingStatus
     */
    public void handleRecording(String recordingUrl,String callSid,String recordingStatus ,String encodedAuth);
}
