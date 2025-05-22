package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.dtos.request.EventRecordRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventRecordResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AIProvider;
import com.cspl.common.gen_ai.speechaiengine.models.enums.DialerType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
public class EventRecordMapperImpl implements EventRecordMapper {

    @Override
    public EventRecord toEntity(EventRecordRequestDto eventRecordRequestDto) {
        if ( eventRecordRequestDto == null ) {
            return null;
        }

        EventRecord eventRecord = new EventRecord();

        eventRecord.setToPhoneNumber( eventRecordRequestDto.getToPhoneNumber() );
        eventRecord.setDialerType( eventRecordRequestDto.getDialerType() );
        eventRecord.setFromPhoneNumber( eventRecordRequestDto.getFromPhoneNumber() );
        eventRecord.setEventStatus( eventRecordRequestDto.getEventStatus() );
        eventRecord.setMaxAttempts( eventRecordRequestDto.getMaxAttempts() );
        eventRecord.setBackOffTimeInMinutes( eventRecordRequestDto.getBackOffTimeInMinutes() );
        if ( eventRecordRequestDto.getDailyStartTime() != null ) {
            eventRecord.setDailyStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( eventRecordRequestDto.getDailyStartTime() ) );
        }
        if ( eventRecordRequestDto.getDailyStopTime() != null ) {
            eventRecord.setDailyStopTime( DateTimeFormatter.ISO_LOCAL_TIME.format( eventRecordRequestDto.getDailyStopTime() ) );
        }
        if ( eventRecordRequestDto.getStartDate() != null ) {
            eventRecord.setStartDate( DateTimeFormatter.ISO_LOCAL_DATE.format( eventRecordRequestDto.getStartDate() ) );
        }
        if ( eventRecordRequestDto.getEndDate() != null ) {
            eventRecord.setEndDate( DateTimeFormatter.ISO_LOCAL_DATE.format( eventRecordRequestDto.getEndDate() ) );
        }
        eventRecord.setAgentId( eventRecordRequestDto.getAgentId() );
        Map<String, Object> map = eventRecordRequestDto.getRequestData();
        if ( map != null ) {
            eventRecord.setRequestData( new HashMap<String, Object>( map ) );
        }

        return eventRecord;
    }

    @Override
    public EventRecordRequestDto toRequestDto(EventRecord eventRecord) {
        if ( eventRecord == null ) {
            return null;
        }

        String toPhoneNumber = null;
        DialerType dialerType = null;
        String fromPhoneNumber = null;
        EventStatus eventStatus = null;
        Integer maxAttempts = null;
        Integer backOffTimeInMinutes = null;
        LocalTime dailyStartTime = null;
        LocalTime dailyStopTime = null;
        Map<String, Object> requestData = null;
        LocalDate startDate = null;
        LocalDate endDate = null;
        String agentId = null;

        toPhoneNumber = eventRecord.getToPhoneNumber();
        dialerType = eventRecord.getDialerType();
        fromPhoneNumber = eventRecord.getFromPhoneNumber();
        eventStatus = eventRecord.getEventStatus();
        maxAttempts = eventRecord.getMaxAttempts();
        backOffTimeInMinutes = eventRecord.getBackOffTimeInMinutes();
        if ( eventRecord.getDailyStartTime() != null ) {
            dailyStartTime = LocalTime.parse( eventRecord.getDailyStartTime() );
        }
        if ( eventRecord.getDailyStopTime() != null ) {
            dailyStopTime = LocalTime.parse( eventRecord.getDailyStopTime() );
        }
        Map<String, Object> map = eventRecord.getRequestData();
        if ( map != null ) {
            requestData = new HashMap<String, Object>( map );
        }
        if ( eventRecord.getStartDate() != null ) {
            startDate = LocalDate.parse( eventRecord.getStartDate() );
        }
        if ( eventRecord.getEndDate() != null ) {
            endDate = LocalDate.parse( eventRecord.getEndDate() );
        }
        agentId = eventRecord.getAgentId();

        String zoneId = null;
        AIProvider aiProvider = null;

        EventRecordRequestDto eventRecordRequestDto = new EventRecordRequestDto( toPhoneNumber, dialerType, fromPhoneNumber, eventStatus, maxAttempts, backOffTimeInMinutes, dailyStartTime, dailyStopTime, requestData, startDate, endDate, zoneId, agentId, aiProvider );

        return eventRecordRequestDto;
    }

    @Override
    public EventRecordResponseDto toResponseDto(EventRecord eventRecord) {
        if ( eventRecord == null ) {
            return null;
        }

        EventRecordResponseDto eventRecordResponseDto = new EventRecordResponseDto();

        eventRecordResponseDto.setId( eventRecord.getId() );
        eventRecordResponseDto.setToPhoneNumber( eventRecord.getToPhoneNumber() );
        eventRecordResponseDto.setDialerType( eventRecord.getDialerType() );
        eventRecordResponseDto.setFromPhoneNumber( eventRecord.getFromPhoneNumber() );
        eventRecordResponseDto.setEventStatus( eventRecord.getEventStatus() );
        eventRecordResponseDto.setMaxAttempts( eventRecord.getMaxAttempts() );
        eventRecordResponseDto.setBackOffTimeInMinutes( eventRecord.getBackOffTimeInMinutes() );
        if ( eventRecord.getStartDate() != null ) {
            eventRecordResponseDto.setStartDate( LocalDate.parse( eventRecord.getStartDate() ) );
        }
        if ( eventRecord.getEndDate() != null ) {
            eventRecordResponseDto.setEndDate( LocalDate.parse( eventRecord.getEndDate() ) );
        }
        if ( eventRecord.getDailyStartTime() != null ) {
            eventRecordResponseDto.setDailyStartTime( LocalTime.parse( eventRecord.getDailyStartTime() ) );
        }
        if ( eventRecord.getDailyStopTime() != null ) {
            eventRecordResponseDto.setDailyStopTime( LocalTime.parse( eventRecord.getDailyStopTime() ) );
        }
        eventRecordResponseDto.setAgentId( eventRecord.getAgentId() );
        eventRecordResponseDto.setMetaData( eventRecord.getMetaData() );

        return eventRecordResponseDto;
    }

    @Override
    public EventRecord partialUpdate(EventRecordRequestDto eventRecordRequestDto, EventRecord eventRecord) {
        if ( eventRecordRequestDto == null ) {
            return null;
        }

        if ( eventRecordRequestDto.getToPhoneNumber() != null ) {
            eventRecord.setToPhoneNumber( eventRecordRequestDto.getToPhoneNumber() );
        }
        if ( eventRecordRequestDto.getDialerType() != null ) {
            eventRecord.setDialerType( eventRecordRequestDto.getDialerType() );
        }
        if ( eventRecordRequestDto.getFromPhoneNumber() != null ) {
            eventRecord.setFromPhoneNumber( eventRecordRequestDto.getFromPhoneNumber() );
        }
        if ( eventRecordRequestDto.getEventStatus() != null ) {
            eventRecord.setEventStatus( eventRecordRequestDto.getEventStatus() );
        }
        if ( eventRecordRequestDto.getMaxAttempts() != null ) {
            eventRecord.setMaxAttempts( eventRecordRequestDto.getMaxAttempts() );
        }
        if ( eventRecordRequestDto.getBackOffTimeInMinutes() != null ) {
            eventRecord.setBackOffTimeInMinutes( eventRecordRequestDto.getBackOffTimeInMinutes() );
        }
        if ( eventRecordRequestDto.getDailyStartTime() != null ) {
            eventRecord.setDailyStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( eventRecordRequestDto.getDailyStartTime() ) );
        }
        if ( eventRecordRequestDto.getDailyStopTime() != null ) {
            eventRecord.setDailyStopTime( DateTimeFormatter.ISO_LOCAL_TIME.format( eventRecordRequestDto.getDailyStopTime() ) );
        }
        if ( eventRecordRequestDto.getStartDate() != null ) {
            eventRecord.setStartDate( DateTimeFormatter.ISO_LOCAL_DATE.format( eventRecordRequestDto.getStartDate() ) );
        }
        if ( eventRecordRequestDto.getEndDate() != null ) {
            eventRecord.setEndDate( DateTimeFormatter.ISO_LOCAL_DATE.format( eventRecordRequestDto.getEndDate() ) );
        }
        if ( eventRecordRequestDto.getAgentId() != null ) {
            eventRecord.setAgentId( eventRecordRequestDto.getAgentId() );
        }
        if ( eventRecord.getRequestData() != null ) {
            Map<String, Object> map = eventRecordRequestDto.getRequestData();
            if ( map != null ) {
                eventRecord.getRequestData().clear();
                eventRecord.getRequestData().putAll( map );
            }
        }
        else {
            Map<String, Object> map = eventRecordRequestDto.getRequestData();
            if ( map != null ) {
                eventRecord.setRequestData( new HashMap<String, Object>( map ) );
            }
        }

        return eventRecord;
    }
}
