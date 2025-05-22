package com.cspl.common.gen_ai.speechaiengine.exceptions;

import com.cspl.common.gen_ai.speechaiengine.dtos.response.BaseRestResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * The type Custom auth exception.
 *
 * @author: Rajkaran
 * @use:
 */
@Getter
public class CustomAuthException extends RuntimeException {

    /**
     * The args
     */
    private Object[] args;

    /**
     * The httpstatus of the exception
     */
    private HttpStatus httpStatus;

    /**
     * The baseresponse
     */
    private BaseRestResponse response;

    /**
     *
     * @param code
     * @param httpStatus
     */
    public CustomAuthException(String code, HttpStatus httpStatus){
        super(code);
        this.httpStatus = httpStatus;
    }

    /**
     *
     * @param code
     * @param httpStatus
     * @param args
     */
    public CustomAuthException(String code, HttpStatus httpStatus,Object... args){
        super(code);
        this.httpStatus = httpStatus;
        this.args = args;
    }

    /**
     *
     * @param code Error Code
     * @param response Error Response
     * @param args Error Arguments
     */
    public CustomAuthException(String code,HttpStatus httpStatus, BaseRestResponse response, Object... args) {
        super(code);
        this.response = response;
        this.httpStatus = httpStatus;
        this.args = args;
    }
}

