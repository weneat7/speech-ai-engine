package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.models.entities.enums.DataType;
import com.cspl.common.gen_ai.speechaiengine.models.enums.AiModel;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "agent")
public class Agent extends AbstractBaseAuditableEntity<String> {

    private String agentId;

    private String agentName;

    private String agentLanguage;

    private String prompt;

    private Map<DataType,Object> dynamicInputs;

    private AiModel aiModel;

    private Object voiceDetector;

    private String speechToTextGenerator;

    private String textToSpeechGenerator;

    private Boolean deleted = false;

    @DBRef
    private List<FunctionTools> functionTools;

    Map<String,Object> sessionDataMapping;

}
