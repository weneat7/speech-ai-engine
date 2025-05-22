package com.cspl.common.gen_ai.speechaiengine.controller.v1;

import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.dtos.request.EventRecordRequestDto;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.EventRecordResponseDto;
import com.cspl.common.gen_ai.speechaiengine.mappers.EventRecordMapper;
import com.cspl.common.gen_ai.speechaiengine.models.entities.EventRecord;
import com.cspl.common.gen_ai.speechaiengine.schedulers.EventRecordsProcessingScheduler;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordService;
import com.cspl.common.gen_ai.speechaiengine.services.IEventRecordValidationService;
import com.cspl.common.gen_ai.speechaiengine.services.selectors.ICallingServiceFactory;
import com.cspl.common.gen_ai.speechaiengine.utils.ApplicationUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller class for EventRecord
 * This class will handle all the requests related to EventRecord
 */
@RestController
@RequestMapping(APIConstants.BASE_URL + APIConstants.API_VERSION.V1 + APIConstants.EVENT_RECORD)
@AllArgsConstructor
public class EventRecordController {

    /**
     * The application utils.
     */
    private final ApplicationUtils applicationUtils;

    /**
     * The event record service.
     */
    private final IEventRecordService eventRecordService;

    private final ICallingServiceFactory callingServiceFactory;

    private final EventRecordMapper eventRecordMapper;

    private final IEventRecordValidationService eventRecordValidationService;

    private final EventRecordsProcessingScheduler eventRecordsProcessingScheduler;

    /**
     * Create event record response entity.
     *
     * @param eventRecordRequestDto the event record request dto
     * @param headers               the headers
     * @return the response entity
     */
    @PostMapping
    @CrossOrigin("*")
    public ResponseEntity<EventRecordResponseDto>
    createEventRecord(@Valid @RequestBody EventRecordRequestDto eventRecordRequestDto, @RequestHeader Map<String,String> headers) {
        return ResponseEntity.ok().body(eventRecordService.createEventRecord(eventRecordRequestDto, headers));
    }

    /**
     * Get event record response entity.
     *
     * @param eventId the event id
     * @param headers the headers
     * @return the response entity
     */
    @GetMapping
    @CrossOrigin("*")
    public ResponseEntity<EventRecordResponseDto>
    getEventRecord(@RequestParam(name = "eventId", required = true) String eventId, @RequestHeader Map<String,String> headers) {
        return ResponseEntity.ok().body(eventRecordService.getEventRecord(eventId, headers));
    }

    /**
     * Update event record response entity.
     *
     * @param eventId               the event id
     * @param eventRecordRequestDto the event record request dto
     * @param headers               the headers
     * @return the response entity
     */
    @PatchMapping
    @CrossOrigin("*")
    public ResponseEntity<EventRecordResponseDto>
    updateEventRecord(@RequestParam("eventId") String eventId, @Valid @RequestBody EventRecordRequestDto eventRecordRequestDto,
                      @RequestHeader Map<String,String> headers) {
        return ResponseEntity.ok().body(eventRecordService.updateEventRecord(eventId, eventRecordRequestDto, headers));
    }

    /**
     * Delete event record response entity.
     *
     * @param eventId the event id
     * @param headers the headers
     * @return the response entity
     */
    @DeleteMapping
    @CrossOrigin("*")
    public ResponseEntity<String>
    deleteEventRecord(@RequestParam(name = "eventId", required = true) String eventId, @RequestHeader Map<String,String> headers) {
        return ResponseEntity.ok().body(eventRecordService.deleteEventRecord(eventId, headers));
    }

    /**
     * to create a call from eventRecord without queueing
     * @param eventRecordRequestDto
     * @throws Exception
     */
    @PostMapping("/call")
    @CrossOrigin("*")
    public void createCall(@RequestBody EventRecordRequestDto eventRecordRequestDto) throws Exception {
        if(eventRecordValidationService.validateEventRecordCreation(eventRecordMapper.toEntity(eventRecordRequestDto))) {
            callingServiceFactory.getCallingService(eventRecordRequestDto.getDialerType()).ifPresent(
                    providerService -> {
                        try {
                            providerService.initiateCallFlowEvent(eventRecordMapper.toEntity(eventRecordRequestDto));
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/*")
    public ResponseEntity<List<EventRecord>> getAllCampaignLeads(@NotNull @RequestParam(name = "campaignId") String campaignId,
                                                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size,
                                                                 @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                                                 @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
                                                                 @RequestHeader Map<String,String> headers) throws Exception {
        return ResponseEntity.ok().body(eventRecordService.getAllEventLeads(campaignId,page,size,sortBy,sortDir));
    }

    @GetMapping("/process")
    public ResponseEntity<String> processEventRecords(){
        eventRecordsProcessingScheduler.processEventRecordsAsync();
        return ResponseEntity.ok().body("Event records processing started");
    }
}
