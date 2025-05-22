package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.controller.v1.EventLeadRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventLead;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface EventLeadMapper {
    EventLead toEntity(EventLeadRequestDto eventLeadRequestDto);

    EventLeadRequestDto toDto(EventLead eventLead);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    EventLead partialUpdate(EventLeadRequestDto eventLeadRequestDto, @MappingTarget EventLead eventLead);

    EventLeadResponseDTO toResponseDto(EventLead eventLead);
}