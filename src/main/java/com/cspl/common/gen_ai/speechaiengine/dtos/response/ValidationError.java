package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * The Class ValidationError.
 */
@NoArgsConstructor
@Data
@AllArgsConstructor

/**
 * The Class ValidationErrorBuilder.
 */
@Builder
public class ValidationError implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -5432214354081137943L;

    /** The path. */
    private String path;

    /** The code. */
    private String code;

    /** The reference value. */
    @JsonIgnore
    private Object referenceValue;

    /** The error. */
    private String error;
}

