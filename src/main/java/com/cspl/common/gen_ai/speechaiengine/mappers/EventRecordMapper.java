package com.cspl.common.gen_ai.speechaiengine.mappers;


import com.cspl.common.gen_ai.speechaiengine.dtos.request.EventRecordRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventRecordResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import org.mapstruct.*;

/**
 * @author vineet.rajput
 * @use: Mapper for {@link EventRecord}
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",builder = @Builder(disableBuilder = true) )
public interface EventRecordMapper {

    /**
     * @param eventRecordRequestDto
     * @return EventRecord
     */
    public EventRecord toEntity(EventRecordRequestDto eventRecordRequestDto);

    /**
     * @param eventRecord
     * @return EventRecordRequestDto
     */
    public EventRecordRequestDto toRequestDto(EventRecord eventRecord);

//    /**
//     * @param eventRecordResponseDto
//     * @return EventRecord
//     */
//    public EventRecord toEntity(EventRecordResponseDto eventRecordRequestDto);

    /**
     * @param eventRecord
     * @return EventRecordResponseDto
     */
    @Mapping(source = "id", target = "id")
    public EventRecordResponseDto toResponseDto(EventRecord eventRecord);

    /**
     * @param eventRecordRequestDto
     * @param eventRecord
     * @return EventRecord
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public EventRecord partialUpdate(EventRecordRequestDto eventRecordRequestDto, @MappingTarget EventRecord eventRecord);

}
