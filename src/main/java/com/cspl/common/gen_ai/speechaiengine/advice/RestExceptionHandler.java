package com.cspl.common.gen_ai.speechaiengine.advice;

import com.cspl.common.gen_ai.speechaiengine.dtos.response.BaseRestResponse;
import com.cspl.common.gen_ai.speechaiengine.dtos.response.ValidationError;
import com.cspl.common.gen_ai.speechaiengine.exceptions.RestServiceException;
import com.cspl.common.gen_ai.speechaiengine.utils.ErrorCodeDetails;
import com.cspl.common.gen_ai.speechaiengine.utils.IErrorCodeUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Slf4j
@ControllerAdvice
@AllArgsConstructor
public class RestExceptionHandler {

        public static final String ERROR_SUFFIX = ".ERROR";

        private IErrorCodeUtils errorCodeUtils;

        private ResourceBundleMessageSource messageSource;

    /**
     * Used to create BaseRestResponse
     *
     * @param errorCode Error Code
     * @param message Error Message
     * @param devErrorMessage System Generated Error Message
     * @param injectDevErrorMessage Flag to enable/disable devErrorMessage
     * @return
     */
    public static BaseRestResponse setBaseRestResponse(String errorCode, String message, String devErrorMessage, boolean injectDevErrorMessage, Set<ValidationError> validationErrors) {
        return setBaseRestResponse(errorCode, message, devErrorMessage, validationErrors, injectDevErrorMessage, new BaseRestResponse());
    }

    /**
     * Used to create BaseRestResponse
     *
     * @param errorCode Error Code
     * @param message Error Message
     * @param devErrorMessage System Generated Error Message
     * @param injectDevErrorMessage Flag to enable/disable devErrorMessage
     * @param response Response
     * @return
     */
    public static BaseRestResponse setBaseRestResponse(String errorCode, String message, String devErrorMessage, Set<ValidationError> validationErrors, boolean injectDevErrorMessage, BaseRestResponse response) {
        if(response == null) {
            response = new BaseRestResponse();
        }
        response.setSuccess(false);
        response.setCode(errorCode);
        response.setMsg(message);
        response.setValidationError(validationErrors);
        if (injectDevErrorMessage) {
            response.setDevErrorMessage(devErrorMessage);
        }
        return response;
    }

        /**
         * Handle RestServiceException
         *
         * @param e RestServiceException
         * @return ResponseEntity with BaseRestResponse
         */
        @ExceptionHandler({RestServiceException.class})
        @ResponseBody
        public ResponseEntity<Object> handleRestServiceException(RestServiceException e) {
            String code = e.getMessage();
            ErrorCodeDetails errorCodeDetails = this.errorCodeUtils.getErrorCodeDetail(code);
            Locale locale = LocaleContextHolder.getLocale();
            String msg =  this.messageSource.getMessage(code + ERROR_SUFFIX, e.getArgs(),locale);
            log.error("Rest Service Exception with code: {} and message: {}", e.getMessage(), msg);
            BaseRestResponse baseResponse = setBaseRestResponse(e.getMessage(), msg, null, null, false, e.getResponse());
            return new ResponseEntity<>(baseResponse, Objects.isNull(errorCodeDetails) ? HttpStatus.INTERNAL_SERVER_ERROR : errorCodeDetails.getHttpStatus());
        }
}