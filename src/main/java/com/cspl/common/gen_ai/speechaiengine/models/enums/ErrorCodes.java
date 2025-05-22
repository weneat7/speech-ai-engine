package com.cspl.common.gen_ai.speechaiengine.models.enums;

import com.cspl.common.gen_ai.speechaiengine.utils.ErrorCodeDetails;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCodes implements ErrorCodeDetails{

    UNAUTHORIZED("SAE_401",HttpStatus.UNAUTHORIZED),



    //Error related to Campaigns
    CAMPAIGN_NOT_FOUND("SAE_CMP_NF_404",HttpStatus.NOT_FOUND),
    CAMPAIGN_STATUS_PARTIALLY_CREATED("SAE_CMP_PCR_400",HttpStatus.BAD_REQUEST);



    private final String code;

    private HttpStatus httpStatus;

    ErrorCodes(String code, HttpStatus httpStatus) {
        this.code = code;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets error code.
     *
     * @param code the code
     * @return the error code
     */
    public static ErrorCodeDetails getErrorCode(String code) {
        for (ErrorCodes errorCode : ErrorCodes.values()) {
            if (errorCode.getCode().equals(code)) {
                return errorCode;
            }
        }
        return null;
    }
}
