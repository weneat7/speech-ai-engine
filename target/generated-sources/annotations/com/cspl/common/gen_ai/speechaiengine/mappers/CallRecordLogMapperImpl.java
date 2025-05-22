package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO.CallRecordingLogDTOBuilder;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog.CallRecordLogBuilder;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T10:48:06+0530",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class CallRecordLogMapperImpl implements CallRecordLogMapper {

    @Override
    public CallRecordLog toEntity(CallRecordingLogDTO callRecordingLogDTO) {
        if ( callRecordingLogDTO == null ) {
            return null;
        }

        CallRecordLogBuilder callRecordLog = CallRecordLog.builder();

        callRecordLog.callSid( callRecordingLogDTO.getCallSid() );
        callRecordLog.callStatus( callRecordingLogDTO.getCallStatus() );
        callRecordLog.recordingUrl( callRecordingLogDTO.getRecordingUrl() );
        callRecordLog.callDetailsDto( callRecordingLogDTO.getCallDetailsDto() );
        Map<String, Object> map = callRecordingLogDTO.getConversationResult();
        if ( map != null ) {
            callRecordLog.conversationResult( new HashMap<String, Object>( map ) );
        }

        return callRecordLog.build();
    }

    @Override
    public CallRecordingLogDTO toCallRecordingDTO(CallRecordLog callRecordLog) {
        if ( callRecordLog == null ) {
            return null;
        }

        CallRecordingLogDTOBuilder callRecordingLogDTO = CallRecordingLogDTO.builder();

        callRecordingLogDTO.callSid( callRecordLog.getCallSid() );
        callRecordingLogDTO.callStatus( callRecordLog.getCallStatus() );
        callRecordingLogDTO.recordingUrl( callRecordLog.getRecordingUrl() );
        callRecordingLogDTO.callDetailsDto( callRecordLog.getCallDetailsDto() );
        Map<String, Object> map = callRecordLog.getConversationResult();
        if ( map != null ) {
            callRecordingLogDTO.conversationResult( new HashMap<String, Object>( map ) );
        }

        return callRecordingLogDTO.build();
    }

    @Override
    public CallRecordLog partialUpdate(CallRecordingLogDTO callRecordingLogDTO, CallRecordLog callRecordLog) {
        if ( callRecordingLogDTO == null ) {
            return null;
        }

        if ( callRecordingLogDTO.getCallSid() != null ) {
            callRecordLog.setCallSid( callRecordingLogDTO.getCallSid() );
        }
        if ( callRecordingLogDTO.getCallStatus() != null ) {
            callRecordLog.setCallStatus( callRecordingLogDTO.getCallStatus() );
        }
        if ( callRecordingLogDTO.getRecordingUrl() != null ) {
            callRecordLog.setRecordingUrl( callRecordingLogDTO.getRecordingUrl() );
        }
        if ( callRecordingLogDTO.getCallDetailsDto() != null ) {
            callRecordLog.setCallDetailsDto( callRecordingLogDTO.getCallDetailsDto() );
        }
        if ( callRecordLog.getConversationResult() != null ) {
            Map<String, Object> map = callRecordingLogDTO.getConversationResult();
            if ( map != null ) {
                callRecordLog.getConversationResult().clear();
                callRecordLog.getConversationResult().putAll( map );
            }
        }
        else {
            Map<String, Object> map = callRecordingLogDTO.getConversationResult();
            if ( map != null ) {
                callRecordLog.setConversationResult( new HashMap<String, Object>( map ) );
            }
        }

        return callRecordLog;
    }
}
