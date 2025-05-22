package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventLeadResponseDTO;
import com.cspl.common.gen_ai.speechaiengine.services.IEventLeadService;
import com.cspl.common.gen_ai.speechaiengine.utils.ApplicationUtils;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

/**
 * The type Event leads controller.
 * @author : Vineet Rajput
 */
@RestController
@RequestMapping(APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.EVENT_LEADS)
@AllArgsConstructor
public class EventLeadsController {

    /**
     * The Event lead service.
     */
    private final IEventLeadService eventLeadService;

    /**
     * The Application utils.
     */
    private final ApplicationUtils applicationUtils;

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/upload-file", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadCsvAsSingleJson(@RequestPart String campaignId,
                                                        @RequestPart("file") Mono<FilePart> filePartMono) {
        return ResponseEntity.ok().body(eventLeadService.processCsvFileAsString(campaignId,filePartMono).block());
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/*")
    public ResponseEntity<Page<EventLeadResponseDTO>> getAllCampaignLeads(@NotNull  @RequestParam(name = "campaignId") String campaignId,
                                                                          @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                          @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                          @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                                          @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                                                          @RequestHeader Map<String,String> headers) throws Exception {
        return ResponseEntity.ok().body(eventLeadService.getAllEventLeads(campaignId,page,size,sortBy,sortDir));
    }

    @CrossOrigin(origins = "*")
    @PostMapping
    public ResponseEntity createLeads(@NotNull @RequestParam(name= "campaignId") String campaignId , @RequestBody List<EventLeadRequestDto> eventLeadRequestDtos) throws Exception {
        eventLeadService.uploadEventLeads(campaignId,eventLeadRequestDtos);
        return ResponseEntity.ok().build();
    }
}
