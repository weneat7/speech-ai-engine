package com.cspl.common.gen_ai.speechaiengine.mappers;

import com.cspl.common.gen_ai.speechaiengine.dtos.request.CampaignRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.CampaignResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CommunicationType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-05-22T10:48:06+0530",
    comments = "version: 1.4.1.Final, compiler: javac, environment: Java 17.0.12 (Amazon.com Inc.)"
)
@Component
public class CampaignMapperImpl implements CampaignMapper {

    @Override
    public Campaign toEntity(CampaignRequestDto campaignRequestDto) {
        if ( campaignRequestDto == null ) {
            return null;
        }

        Campaign campaign = new Campaign();

        campaign.setName( campaignRequestDto.getName() );
        campaign.setDescription( campaignRequestDto.getDescription() );
        campaign.setAgentId( campaignRequestDto.getAgentId() );
        campaign.setAiProvider( campaignRequestDto.getAiProvider() );
        campaign.setCampaignType( campaignRequestDto.getCampaignType() );
        List<CommunicationType> list = campaignRequestDto.getCommunicationType();
        if ( list != null ) {
            campaign.setCommunicationType( new ArrayList<CommunicationType>( list ) );
        }
        campaign.setCampaignStatus( campaignRequestDto.getCampaignStatus() );
        if ( campaignRequestDto.getStartDate() != null ) {
            campaign.setStartDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignRequestDto.getStartDate() ) );
        }
        if ( campaignRequestDto.getEndDate() != null ) {
            campaign.setEndDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignRequestDto.getEndDate() ) );
        }
        if ( campaignRequestDto.getDailyStartTime() != null ) {
            campaign.setDailyStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignRequestDto.getDailyStartTime() ) );
        }
        if ( campaignRequestDto.getDailyStopTime() != null ) {
            campaign.setDailyStopTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignRequestDto.getDailyStopTime() ) );
        }
        campaign.setDialer( campaignRequestDto.getDialer() );
        List<String> list1 = campaignRequestDto.getFromPhoneNumber();
        if ( list1 != null ) {
            campaign.setFromPhoneNumber( new ArrayList<String>( list1 ) );
        }
        campaign.setMaxAttempts( campaignRequestDto.getMaxAttempts() );
        campaign.setDailyAttempts( campaignRequestDto.getDailyAttempts() );
        campaign.setWeeklyAttempts( campaignRequestDto.getWeeklyAttempts() );
        campaign.setBackOffTimeInMinutes( campaignRequestDto.getBackOffTimeInMinutes() );
        List<String> list2 = campaignRequestDto.getRuleId();
        if ( list2 != null ) {
            campaign.setRuleId( new ArrayList<String>( list2 ) );
        }
        campaign.setZoneId( campaignRequestDto.getZoneId() );

        return campaign;
    }

    @Override
    public CampaignRequestDto toRequestDto(Campaign campaign) {
        if ( campaign == null ) {
            return null;
        }

        CampaignRequestDto campaignRequestDto = new CampaignRequestDto();

        campaignRequestDto.setName( campaign.getName() );
        campaignRequestDto.setDescription( campaign.getDescription() );
        campaignRequestDto.setAgentId( campaign.getAgentId() );
        campaignRequestDto.setCampaignType( campaign.getCampaignType() );
        campaignRequestDto.setCampaignStatus( campaign.getCampaignStatus() );
        List<CommunicationType> list = campaign.getCommunicationType();
        if ( list != null ) {
            campaignRequestDto.setCommunicationType( new ArrayList<CommunicationType>( list ) );
        }
        if ( campaign.getStartDate() != null ) {
            campaignRequestDto.setStartDate( LocalDate.parse( campaign.getStartDate() ) );
        }
        if ( campaign.getEndDate() != null ) {
            campaignRequestDto.setEndDate( LocalDate.parse( campaign.getEndDate() ) );
        }
        if ( campaign.getDailyStartTime() != null ) {
            campaignRequestDto.setDailyStartTime( LocalTime.parse( campaign.getDailyStartTime() ) );
        }
        if ( campaign.getDailyStopTime() != null ) {
            campaignRequestDto.setDailyStopTime( LocalTime.parse( campaign.getDailyStopTime() ) );
        }
        campaignRequestDto.setZoneId( campaign.getZoneId() );
        campaignRequestDto.setDialer( campaign.getDialer() );
        campaignRequestDto.setMaxAttempts( campaign.getMaxAttempts() );
        campaignRequestDto.setDailyAttempts( campaign.getDailyAttempts() );
        campaignRequestDto.setWeeklyAttempts( campaign.getWeeklyAttempts() );
        List<String> list1 = campaign.getRuleId();
        if ( list1 != null ) {
            campaignRequestDto.setRuleId( new ArrayList<String>( list1 ) );
        }
        campaignRequestDto.setBackOffTimeInMinutes( campaign.getBackOffTimeInMinutes() );
        List<String> list2 = campaign.getFromPhoneNumber();
        if ( list2 != null ) {
            campaignRequestDto.setFromPhoneNumber( new ArrayList<String>( list2 ) );
        }
        campaignRequestDto.setAiProvider( campaign.getAiProvider() );

        return campaignRequestDto;
    }

    @Override
    public Campaign toEntity(CampaignResponseDto campaignResponseDto) {
        if ( campaignResponseDto == null ) {
            return null;
        }

        Campaign campaign = new Campaign();

        campaign.setId( campaignResponseDto.getId() );
        if ( campaignResponseDto.getCreatedAt() != null ) {
            campaign.setCreatedAt( LocalDateTime.parse( campaignResponseDto.getCreatedAt() ) );
        }
        campaign.setName( campaignResponseDto.getName() );
        campaign.setDescription( campaignResponseDto.getDescription() );
        campaign.setAgentId( campaignResponseDto.getAgentId() );
        campaign.setCampaignType( campaignResponseDto.getCampaignType() );
        List<CommunicationType> list = campaignResponseDto.getCommunicationType();
        if ( list != null ) {
            campaign.setCommunicationType( new ArrayList<CommunicationType>( list ) );
        }
        campaign.setCampaignStatus( campaignResponseDto.getCampaignStatus() );
        if ( campaignResponseDto.getStartDate() != null ) {
            campaign.setStartDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignResponseDto.getStartDate() ) );
        }
        if ( campaignResponseDto.getEndDate() != null ) {
            campaign.setEndDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignResponseDto.getEndDate() ) );
        }
        if ( campaignResponseDto.getDailyStartTime() != null ) {
            campaign.setDailyStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignResponseDto.getDailyStartTime() ) );
        }
        if ( campaignResponseDto.getDailyStopTime() != null ) {
            campaign.setDailyStopTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignResponseDto.getDailyStopTime() ) );
        }
        campaign.setDialer( campaignResponseDto.getDialer() );
        List<String> list1 = campaignResponseDto.getFromPhoneNumber();
        if ( list1 != null ) {
            campaign.setFromPhoneNumber( new ArrayList<String>( list1 ) );
        }
        campaign.setMaxAttempts( campaignResponseDto.getMaxAttempts() );
        campaign.setDailyAttempts( campaignResponseDto.getDailyAttempts() );
        campaign.setWeeklyAttempts( campaignResponseDto.getWeeklyAttempts() );
        campaign.setBackOffTimeInMinutes( campaignResponseDto.getBackOffTimeInMinutes() );
        List<String> list2 = campaignResponseDto.getRuleId();
        if ( list2 != null ) {
            campaign.setRuleId( new ArrayList<String>( list2 ) );
        }
        campaign.setZoneId( campaignResponseDto.getZoneId() );
        campaign.setMetadata( campaignResponseDto.getMetadata() );

        return campaign;
    }

    @Override
    public CampaignResponseDto entityToResponseDto(Campaign campaign) {
        if ( campaign == null ) {
            return null;
        }

        CampaignResponseDto campaignResponseDto = new CampaignResponseDto();

        campaignResponseDto.setId( campaign.getId() );
        campaignResponseDto.setName( campaign.getName() );
        campaignResponseDto.setDescription( campaign.getDescription() );
        campaignResponseDto.setCampaignType( campaign.getCampaignType() );
        List<CommunicationType> list = campaign.getCommunicationType();
        if ( list != null ) {
            campaignResponseDto.setCommunicationType( new ArrayList<CommunicationType>( list ) );
        }
        campaignResponseDto.setCampaignStatus( campaign.getCampaignStatus() );
        if ( campaign.getStartDate() != null ) {
            campaignResponseDto.setStartDate( LocalDate.parse( campaign.getStartDate() ) );
        }
        if ( campaign.getEndDate() != null ) {
            campaignResponseDto.setEndDate( LocalDate.parse( campaign.getEndDate() ) );
        }
        campaignResponseDto.setDialer( campaign.getDialer() );
        campaignResponseDto.setMaxAttempts( campaign.getMaxAttempts() );
        campaignResponseDto.setDailyAttempts( campaign.getDailyAttempts() );
        campaignResponseDto.setWeeklyAttempts( campaign.getWeeklyAttempts() );
        List<String> list1 = campaign.getRuleId();
        if ( list1 != null ) {
            campaignResponseDto.setRuleId( new ArrayList<String>( list1 ) );
        }
        List<String> list2 = campaign.getFromPhoneNumber();
        if ( list2 != null ) {
            campaignResponseDto.setFromPhoneNumber( new ArrayList<String>( list2 ) );
        }
        campaignResponseDto.setBackOffTimeInMinutes( campaign.getBackOffTimeInMinutes() );
        if ( campaign.getDailyStartTime() != null ) {
            campaignResponseDto.setDailyStartTime( LocalTime.parse( campaign.getDailyStartTime() ) );
        }
        if ( campaign.getDailyStopTime() != null ) {
            campaignResponseDto.setDailyStopTime( LocalTime.parse( campaign.getDailyStopTime() ) );
        }
        campaignResponseDto.setZoneId( campaign.getZoneId() );
        campaignResponseDto.setMetadata( campaign.getMetadata() );
        campaignResponseDto.setAgentId( campaign.getAgentId() );
        if ( campaign.getCreatedAt() != null ) {
            campaignResponseDto.setCreatedAt( DateTimeFormatter.ISO_LOCAL_DATE_TIME.format( campaign.getCreatedAt() ) );
        }

        return campaignResponseDto;
    }

    @Override
    public Campaign partialUpdate(CampaignRequestDto campaignRequestDto, Campaign campaign) {
        if ( campaignRequestDto == null ) {
            return null;
        }

        if ( campaignRequestDto.getName() != null ) {
            campaign.setName( campaignRequestDto.getName() );
        }
        if ( campaignRequestDto.getDescription() != null ) {
            campaign.setDescription( campaignRequestDto.getDescription() );
        }
        if ( campaignRequestDto.getAgentId() != null ) {
            campaign.setAgentId( campaignRequestDto.getAgentId() );
        }
        if ( campaignRequestDto.getAiProvider() != null ) {
            campaign.setAiProvider( campaignRequestDto.getAiProvider() );
        }
        if ( campaignRequestDto.getCampaignType() != null ) {
            campaign.setCampaignType( campaignRequestDto.getCampaignType() );
        }
        if ( campaign.getCommunicationType() != null ) {
            List<CommunicationType> list = campaignRequestDto.getCommunicationType();
            if ( list != null ) {
                campaign.getCommunicationType().clear();
                campaign.getCommunicationType().addAll( list );
            }
        }
        else {
            List<CommunicationType> list = campaignRequestDto.getCommunicationType();
            if ( list != null ) {
                campaign.setCommunicationType( new ArrayList<CommunicationType>( list ) );
            }
        }
        if ( campaignRequestDto.getCampaignStatus() != null ) {
            campaign.setCampaignStatus( campaignRequestDto.getCampaignStatus() );
        }
        if ( campaignRequestDto.getStartDate() != null ) {
            campaign.setStartDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignRequestDto.getStartDate() ) );
        }
        if ( campaignRequestDto.getEndDate() != null ) {
            campaign.setEndDate( DateTimeFormatter.ISO_LOCAL_DATE.format( campaignRequestDto.getEndDate() ) );
        }
        if ( campaignRequestDto.getDailyStartTime() != null ) {
            campaign.setDailyStartTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignRequestDto.getDailyStartTime() ) );
        }
        if ( campaignRequestDto.getDailyStopTime() != null ) {
            campaign.setDailyStopTime( DateTimeFormatter.ISO_LOCAL_TIME.format( campaignRequestDto.getDailyStopTime() ) );
        }
        if ( campaignRequestDto.getDialer() != null ) {
            campaign.setDialer( campaignRequestDto.getDialer() );
        }
        if ( campaign.getFromPhoneNumber() != null ) {
            List<String> list1 = campaignRequestDto.getFromPhoneNumber();
            if ( list1 != null ) {
                campaign.getFromPhoneNumber().clear();
                campaign.getFromPhoneNumber().addAll( list1 );
            }
        }
        else {
            List<String> list1 = campaignRequestDto.getFromPhoneNumber();
            if ( list1 != null ) {
                campaign.setFromPhoneNumber( new ArrayList<String>( list1 ) );
            }
        }
        if ( campaignRequestDto.getMaxAttempts() != null ) {
            campaign.setMaxAttempts( campaignRequestDto.getMaxAttempts() );
        }
        if ( campaignRequestDto.getDailyAttempts() != null ) {
            campaign.setDailyAttempts( campaignRequestDto.getDailyAttempts() );
        }
        if ( campaignRequestDto.getWeeklyAttempts() != null ) {
            campaign.setWeeklyAttempts( campaignRequestDto.getWeeklyAttempts() );
        }
        if ( campaignRequestDto.getBackOffTimeInMinutes() != null ) {
            campaign.setBackOffTimeInMinutes( campaignRequestDto.getBackOffTimeInMinutes() );
        }
        if ( campaign.getRuleId() != null ) {
            List<String> list2 = campaignRequestDto.getRuleId();
            if ( list2 != null ) {
                campaign.getRuleId().clear();
                campaign.getRuleId().addAll( list2 );
            }
        }
        else {
            List<String> list2 = campaignRequestDto.getRuleId();
            if ( list2 != null ) {
                campaign.setRuleId( new ArrayList<String>( list2 ) );
            }
        }
        if ( campaignRequestDto.getZoneId() != null ) {
            campaign.setZoneId( campaignRequestDto.getZoneId() );
        }

        return campaign;
    }
}
