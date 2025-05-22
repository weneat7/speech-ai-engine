package com.cspl.common.gen_ai.speechaiengine.dtos.response;

import java.io.Serializable;
import java.time.Instant;
import java.util.Collection;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Class BaseRestResponse.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder({"st", "msgid", "msg", "devErrorMessage", "timestamp", "validationError"})
public class BaseRestResponse implements Serializable {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 3692959428126102727L;

    /** The success. */
    @JsonProperty("st")
    private boolean success;

    /** The timestamp. */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
    private Instant timestamp = Instant.now();

    /** The code. */
    @JsonProperty("msgid")
    private String code;

    /** The msg. */
    @JsonProperty("msg")
    private String msg;

    /** The dev error message. */
    private String devErrorMessage;

    /** The validation error. */
    @JsonProperty("validationError")
    private Collection<ValidationError> validationError;

    /**
     * Sets the failure response.
     *
     * @param errorCode the error code
     * @param message the message
     * @param devErrorMessage the dev error message
     * @param injectDevErrorMessage the inject dev error message
     * @param validationErrors the validation errors
     */
    public void setFailureResponse(String errorCode, String message, String devErrorMessage,
                                   boolean injectDevErrorMessage, Collection<ValidationError> validationErrors){
        this.setSuccess(false);
        this.setCode(errorCode);
        this.setMsg(message);
        this.setValidationError(validationErrors);
        if (injectDevErrorMessage) {
            this.setDevErrorMessage(devErrorMessage);
        }
    }

    /**
     * Sets the failure response.
     *
     * @param errorCode the error code
     * @param message the message
     */
    public void setFailureResponse(String errorCode, String message){
        this.setSuccess(false);
        this.setCode(errorCode);
        this.setMsg(message);
    }

    /**
     * Sets the success response.
     *
     * @param message the new success response
     */
    public void setSuccessResponse(String message) {
        this.setSuccess(true);
        if (!StringUtils.isEmpty(message)) {
            this.setMsg(message);
        }
    }

    /**
     * Sets the success response.
     *
     * @param code the code
     * @param message the message
     */
    public void setSuccessResponse(String code, String message) {
        this.setCode(code);
        setSuccessResponse(message);
    }

}
