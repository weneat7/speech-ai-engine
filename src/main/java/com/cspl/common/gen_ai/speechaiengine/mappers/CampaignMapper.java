package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.dtos.request.CampaignRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.CampaignResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import org.mapstruct.*;

/**
 * @author vineet.rajput
 * @use: Mapper for {@link Campaign}
 */
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CampaignMapper {

    /**
     * @param campaignRequestDto
     * @return Campaign
     */
    Campaign toEntity(CampaignRequestDto campaignRequestDto);

    /**
     * @param campaign
     * @return CampaignRequestDto
     */
    CampaignRequestDto toRequestDto(Campaign campaign);

    /**
     * @param campaignResponseDto
     * @return Campaign
     */
    Campaign toEntity(CampaignResponseDto campaignResponseDto);

    /**
     * @param campaign
     * @return CampaignResponseDto
     */
    @Mapping(source = "id", target = "id")
    CampaignResponseDto entityToResponseDto(Campaign campaign);

    /**
     * @param campaignRequestDto
     * @param campaign
     * @return Campaign
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Campaign partialUpdate(CampaignRequestDto campaignRequestDto, @MappingTarget Campaign campaign);
}