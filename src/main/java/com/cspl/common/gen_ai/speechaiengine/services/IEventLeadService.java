package com.cspl.common.gen_ai.speechaiengine.services;

import com.cspl.common.gen_ai.speechaiengine.controller.v1.EventLeadRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IEventLeadService {

    /**
     * Save event lead from csv list.
     * @param filePartMono
     * @return
     */
    public Mono<String> processCsvFileAsString(String campaignId, Mono<FilePart> filePartMono);

    /**
     * Upload event leads.
     * @param campaignId
     * @param eventLeadRequestDtoList
     */
    public void uploadEventLeads(String campaignId, List<EventLeadRequestDto> eventLeadRequestDtoList) throws Exception;

    /**
     * to get paginated event leads
     * @param campaignId
     * @param page
     * @param size
     * @param sortBy
     * @param sortDir
     * @return
     * @throws Exception
     */
    public Page<EventLeadResponseDTO> getAllEventLeads(String campaignId, int page, int size, String sortBy, String sortDir) throws Exception;
}
