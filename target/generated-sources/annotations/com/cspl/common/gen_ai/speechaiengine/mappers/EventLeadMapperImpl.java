package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.controller.v1.EventLeadRequestDto;
import com.cspl.common.gen_ai.speechaiengine.controller.v1.EventLeadRequestDto.EventLeadRequestDtoBuilder;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO.EventLeadResponseDTOBuilder;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
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
public class EventLeadMapperImpl implements EventLeadMapper {

    @Override
    public EventLead toEntity(EventLeadRequestDto eventLeadRequestDto) {
        if ( eventLeadRequestDto == null ) {
            return null;
        }

        EventLead eventLead = new EventLead();

        Map<String, Object> map = eventLeadRequestDto.getRequestData();
        if ( map != null ) {
            eventLead.setRequestData( new HashMap<String, Object>( map ) );
        }

        return eventLead;
    }

    @Override
    public EventLeadRequestDto toDto(EventLead eventLead) {
        if ( eventLead == null ) {
            return null;
        }

        EventLeadRequestDtoBuilder eventLeadRequestDto = EventLeadRequestDto.builder();

        Map<String, Object> map = eventLead.getRequestData();
        if ( map != null ) {
            eventLeadRequestDto.requestData( new HashMap<String, Object>( map ) );
        }

        return eventLeadRequestDto.build();
    }

    @Override
    public EventLead partialUpdate(EventLeadRequestDto eventLeadRequestDto, EventLead eventLead) {
        if ( eventLeadRequestDto == null ) {
            return null;
        }

        if ( eventLead.getRequestData() != null ) {
            Map<String, Object> map = eventLeadRequestDto.getRequestData();
            if ( map != null ) {
                eventLead.getRequestData().clear();
                eventLead.getRequestData().putAll( map );
            }
        }
        else {
            Map<String, Object> map = eventLeadRequestDto.getRequestData();
            if ( map != null ) {
                eventLead.setRequestData( new HashMap<String, Object>( map ) );
            }
        }

        return eventLead;
    }

    @Override
    public EventLeadResponseDTO toResponseDto(EventLead eventLead) {
        if ( eventLead == null ) {
            return null;
        }

        EventLeadResponseDTOBuilder eventLeadResponseDTO = EventLeadResponseDTO.builder();

        eventLeadResponseDTO.id( eventLead.getId() );
        Map<String, Object> map = eventLead.getRequestData();
        if ( map != null ) {
            eventLeadResponseDTO.requestData( new HashMap<String, Object>( map ) );
        }

        return eventLeadResponseDTO.build();
    }
}
