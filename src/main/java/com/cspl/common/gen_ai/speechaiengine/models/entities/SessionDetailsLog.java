package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.Provider;
import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.SessionStatus;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "session_details_logs")
@Data
@Builder
public class SessionDetailsLog {
    @Id
    private String sessionId;
    private SessionStatus sessionStatus;
    private String callSid;
    private String streamId;
    private Provider provider;
    private LocalDateTime sessionStartTime;
}
