package com.cspl.common.gen_ai.speechaiengine.services;


import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.cspl.common.gen_ai.speechaiengine.dtos.request.CampaignRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.CampaignResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.api.gax.rpc.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * Service interface for managing campaigns.
 */
public interface ICampaignService {

    /**
     * Fetches a campaign by its ID.
     * @param campaignId
     * @param headers
     * @return
     * @throws Exception
     */
    public CampaignResponseDto getCampaign(String campaignId, boolean metadata, Map<String,String> headers) throws Exception;

    /**
     * Fetches all campaigns with pagination.
     *
     * @param size
     * @param headers
     * @return
     */
    public Page<CampaignResponseDto> getAllCampaigns(int page, int size, String sortBy, String sortDir, boolean metadata, Map<String,String> headers);

    /**
     * Creates a new campaign.
     * @param campaignId
     * @param campaignRequestDto
     * @param headers
     * @return
     * @throws Exception
     */
    public CampaignResponseDto updateCampaign(String campaignId, CampaignRequestDto campaignRequestDto, Map<String,String> headers, boolean completed) throws Exception;

    /**
     * Creates a new campaign.
     * @param campaignRequestDto
     * @param headers
     * @return
     */
    public CampaignResponseDto createCampaign(CampaignRequestDto campaignRequestDto, Map<String,String> headers) throws JsonProcessingException;

    /**
     * Deletes a campaign by its ID.
     * @param campaignId
     * @param headers
     * @return
     */
    public ResponseEntity<String> deleteCampaign(String campaignId, Map<String,String> headers) throws Exception;


    /**
     * Fetches a campaign by its ID.
     * @param campaignId
     * @return
     * @throws Exception
     */
    public Campaign getCampaign(String campaignId) throws Exception;

    /**
     * Updates the status of a campaign.
     * @param campaignId
     * @param status
     * @param headers
     * @return
     * @throws NotFoundException
     */
    public CampaignResponseDto updateCampaignStatus(String campaignId, CampaignStatus status, Map<String,String> headers) throws Exception;

    List<RuleCreationDTO> getAllRulesByCampaign(String campaignId, int page, int size, String sortBy, String sortDir,Map<String,String> headers) throws Exception;
}
