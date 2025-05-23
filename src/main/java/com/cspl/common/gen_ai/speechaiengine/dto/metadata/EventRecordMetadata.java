package com.cspl.common.gen_ai.speechaiengine.dto.metadata;

import com.cspl.common.gen_ai.speechaiengine.models.entities.CallRecordLog;
import lombok.Builder;
import lombok.Data;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

@Data
@Builder
public class EventRecordMetadata {

    @Lazy(value = true)
    @DBRef
    List<CallRecordLog> callRecordLogList;

    private RetryMetadata retryMetadata;
}
