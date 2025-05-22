package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.cspl.common.gen_ai.speechaiengine.models.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventLeadResponseDTO {
    private String id;
    private String recordingUrl;
    private EventStatus eventStatus;
    Map<String,Object> requestData;
}
