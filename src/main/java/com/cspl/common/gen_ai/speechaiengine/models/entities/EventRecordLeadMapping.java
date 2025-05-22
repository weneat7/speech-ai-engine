package com.cspl.common.gen_ai.speechaiengine.models.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document(collection = "event_record_lead_mapping")
public class EventRecordLeadMapping extends AbstractBaseAuditableEntity<String> {

    @DBRef
    private EventLead eventLead;

    @DBRef
    private EventRecord eventRecord;

    private String campaignId;
}
