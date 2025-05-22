package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.dto.RuleCreationDTO;
import com.cspl.common.gen_ai.speechaiengine.dtos.request.CampaignRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.CampaignResponseDto;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.cspl.common.gen_ai.speechaiengine.services.ICampaignService;
import com.cspl.common.gen_ai.speechaiengine.utils.ApplicationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author vineet.rajput
 * Controller class for Campaign
 */
@RestController
@RequestMapping(APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.CAMPAIGN)
@AllArgsConstructor
public class CampaignController {

    /**
     * The application utils.
     */
    private final ApplicationUtils applicationUtils;

    /**
     * The campaign service.
     */
    private final ICampaignService campaignService;

    /**
     * Get campaign response entity.
     *
     * @param campaignId the campaign id
     * @param headers    the headers
     * @return the response entity
     */
    @GetMapping
    public ResponseEntity<CampaignResponseDto>
    getCampaign(@RequestParam(name = "campaignId", required = false) String campaignId,
                @RequestParam(name = "metadata", defaultValue = "false") boolean metadata,
                @RequestHeader Map<String,String> headers) throws Exception {
        return ResponseEntity.ok().body(campaignService.getCampaign(campaignId, metadata, headers));
    }

    /**
     * Get all campaigns response entity
     * @param page     the page
     * @param size     the size
     * @param sortBy   the sort by
     * @param sortDir  the sort dir
     * @param headers  the headers
     * @return the response entity
     */
    @GetMapping("/*")
    @CrossOrigin("*")
    public ResponseEntity<Page<CampaignResponseDto>> getAllCampaigns(@RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                     @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                     @RequestParam(value = "sortBy", required = false, defaultValue = "startDate") String sortBy,
                                                                     @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                                                     @RequestParam(name = "metadata", defaultValue = "false") boolean metadata,
                                                                     @RequestHeader Map<String,String> headers) {
        return ResponseEntity.ok().body(campaignService.getAllCampaigns(page, size, sortBy, sortDir, metadata, headers));
    }

    /**
     * Create campaign response entity.
     *
     * @param campaignRequestDto the campaign request dto
     * @param headers            the headers
     * @return the response entity
     */
    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<CampaignResponseDto>
    createCampaign(@Valid @RequestBody CampaignRequestDto campaignRequestDto, @RequestHeader Map<String,String> headers) throws JsonProcessingException {
        return ResponseEntity.ok().body(campaignService.createCampaign(campaignRequestDto, headers));
    }

    /**
     * Update campaign response entity.
     *
     * @param campaignId         the campaign id
     * @param campaignRequestDto the campaign request dto
     * @param headers            the headers
     * @return the response entity
     */
    @PostMapping("/update")
    @CrossOrigin("*")
    public ResponseEntity<CampaignResponseDto>
    updateCampaign(@RequestParam("campaignId") String campaignId,
                   @RequestParam(value = "completed" , defaultValue = "false") boolean completed,
                   @RequestBody CampaignRequestDto campaignRequestDto,
                   @RequestHeader Map<String,String> headers) throws Exception {
        return ResponseEntity.ok().body(campaignService.updateCampaign(campaignId, campaignRequestDto, headers,completed));
    }

    /**
     * Delete campaign response entity.
     *
     * @param campaignId the campaign id
     * @param headers    the headers
     * @return the response entity
     */
    @DeleteMapping
    @CrossOrigin("*")
    public ResponseEntity<String>
    deleteCampaign(@RequestParam(name = "campaignId", required = true) String campaignId,
                   @RequestHeader Map<String,String> headers) throws Exception {
        return campaignService.deleteCampaign(campaignId, headers);
    }

    @GetMapping("/status")
    @CrossOrigin("*")
    public ResponseEntity<CampaignResponseDto>
    updateCampaign(@RequestParam(value = "campaignId", required = true) String campaignId,
                   @RequestParam(value = "campaignStatus", required = true) CampaignStatus campaignStatus,
                   @RequestHeader Map<String,String> headers) throws Exception {
        List<CampaignStatus> campaignStatuses = List.of(CampaignStatus.ACTIVE, CampaignStatus.PAUSED, CampaignStatus.INACTIVE);
        if (!campaignStatuses.contains(campaignStatus)) {
            throw new IllegalArgumentException("Invalid campaign status, cannot update status as : " + campaignStatus);
        }
        return ResponseEntity.ok().body(campaignService.updateCampaignStatus(campaignId, campaignStatus, headers));
    }

    /**
     * to get paginated rules for a campaign
     * @param campaignId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @param headers
     * @return
     * @throws Exception
     */
    @GetMapping("/rules")
    @CrossOrigin("*")
    public ResponseEntity<List<RuleCreationDTO>> getAllCampaignRules(@NotNull @RequestParam String campaignId,
                                                                     @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                     @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                     @RequestParam(value = "sortBy", required = false, defaultValue = "name") String sortBy,
                                                                     @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                                                     @RequestHeader Map<String,String> headers) throws Exception {
        return ResponseEntity.ok().body(campaignService.getAllRulesByCampaign(campaignId,page,size,sortBy,sortDir,headers));
    }
}