package com.cspl.common.gen_ai.speechaiengine.schedulers;

import com.cspl.common.gen_ai.speechaiengine.models.entities.Campaign;
import com.cspl.common.gen_ai.speechaiengine.models.enums.CampaignStatus;
import com.cspl.common.gen_ai.speechaiengine.repositories.CampaignRepository;
import com.cspl.common.gen_ai.speechaiengine.services.impl.CampaignService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class CampaignProcessingScheduler {
    /**
     * The Campaign Repository
     */
    private final CampaignRepository campaignRepository;

    /**
     * The Campaign Service
     */
    private final CampaignService campaignService;


    /**
     * Method to process the campaign
     * This method will be called every hour
     */
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    private void processCampaign(){
        List<Campaign> campaignList = campaignRepository.findAllByCampaignStatus(CampaignStatus.ACTIVE);
        for(Campaign campaign:campaignList){
            campaignService.processCampaign(campaign);
        }
    }
}