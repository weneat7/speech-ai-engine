package com.cspl.common.gen_ai.speechaiengine.models.entities;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "event_leads")
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventLead extends AbstractBaseAuditableEntity<String> {

    private Map<String,Object> requestData;
}
