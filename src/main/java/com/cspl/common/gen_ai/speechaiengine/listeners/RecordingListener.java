package com.cspl.common.gen_ai.speechaiengine.listeners;

import com.cspl.common.gen_ai.speechaiengine.constants.AppConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.exceptions.RestServiceException;
import com.cspl.common.gen_ai.speechaiengine.mappers.CallRecordLogMapper;
import com.cspl.common.gen_ai.speechaiengine.repositories.CallRecordLogRepository;
import com.cspl.common.gen_ai.speechaiengine.services.IFileCloudTransferService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.sonus21.rqueue.annotation.RqueueListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@AllArgsConstructor
public class RecordingListener {

    IFileCloudTransferService fileCloudTransferService;
    CallRecordLogRepository callRecordLogRepository;
    CallRecordLogMapper callRecordLogMapper;
    ObjectMapper objectMapper;

    @RqueueListener(value = AppConstants.CALL_RECORDING_QUEUE, concurrency = "1", active = "true")
    public void processFileAndSave(String callRecordingLogDTOSt) {
        try {
            CallRecordingLogDTO callRecordingLogDTO = objectMapper.readValue( objectMapper.readTree(callRecordingLogDTOSt).get("msg").asText(), CallRecordingLogDTO.class);
            callRecordingLogDTO.setRecordingUrl(fileCloudTransferService.transferFileFromUrl(callRecordingLogDTO.getRecordingUrl(), callRecordingLogDTO.getCallSid() + ".mp3",callRecordingLogDTO.getEncodedAuth()));
            callRecordLogRepository.save(callRecordLogMapper.partialUpdate(callRecordingLogDTO,callRecordLogRepository.findByCallSid(callRecordingLogDTO.getCallSid())
                                                            .orElseThrow(()->new RestServiceException("No call record found for "+ callRecordingLogDTO.getCallSid()))));
        } catch (Exception e) {
            log.error("Error processing task: {}" , e.getMessage());
        }
    }
}
