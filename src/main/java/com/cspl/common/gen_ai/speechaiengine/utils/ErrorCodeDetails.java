package com.cspl.common.gen_ai.speechaiengine.utils;

import org.springframework.http.HttpStatus;

public interface ErrorCodeDetails {

    public String getCode();

    HttpStatus getHttpStatus();
}
