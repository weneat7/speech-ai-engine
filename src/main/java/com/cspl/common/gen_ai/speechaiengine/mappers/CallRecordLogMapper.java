package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.dto.CallRecordingLogDTO;
import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CallRecordLogMapper {

    /**
     * to convert dto to entity
     * @param callRecordingLogDTO
     * @return
     */
    CallRecordLog toEntity(CallRecordingLogDTO callRecordingLogDTO);

    /**
     * get dto
     * @param callRecordLog
     * @return
     */
    CallRecordingLogDTO toCallRecordingDTO(CallRecordLog callRecordLog);

    /**
     * update from DTO
     * @param callRecordingLogDTO
     * @param callRecordLog
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CallRecordLog partialUpdate(CallRecordingLogDTO callRecordingLogDTO, @MappingTarget CallRecordLog callRecordLog);
}
