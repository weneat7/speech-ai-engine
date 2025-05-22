package com.cspl.common.gen_ai.speechaiengine.exceptions;

import com.cspl.common.gen_ai.speechaiengine.dtos.response.BaseRestResponse;

public class RestServiceException extends RuntimeException {

    private static final long serialVersionUID = -6058067821158675893L;
    private Object[] args;
    private BaseRestResponse response;

    public RestServiceException(String code, Object... args) {
        super(code);
        this.args = args;
    }

    public RestServiceException(String code, BaseRestResponse response, Object... args) {
        super(code);
        this.response = response;
        this.args = args;
    }

    public RestServiceException(String code) {
        super(code);
    }

    public Object[] getArgs() {
        return this.args;
    }

    public BaseRestResponse getResponse() {
        return this.response;
    }
}
