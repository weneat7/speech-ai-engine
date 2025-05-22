package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.cspl.common.gen_ai.speechaiengine.models.enums.FunctionToolType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@EqualsAndHashCode(callSuper = true)
@Document(collection = "function_tools")
public class FunctionTools extends AbstractBaseAuditableEntity<String> {

    private String name;

    private FunctionToolType functionType;

    private String description;

    private Map<String, Object> parameters;

    private List<String> requiredParameters;

    private String flowTemplateName;

}
