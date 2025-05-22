package com.cspl.common.gen_ai.speechaiengine.utils;

import com.cspl.common.gen_ai.speechaiengine.models.enums.ErrorCodes;
import org.springframework.stereotype.Service;

/**
 * The type Error code utils.
 *
 * @author: vineet.rajput
 * @use:
 */
@Service
public class ErrorCodeUtils implements IErrorCodeUtils {

    /**
     * Gets error code detail.
     *
     * @param code the code
     * @return the error code detail
     */
    @Override
    public ErrorCodeDetails getErrorCodeDetail(String code) {
        return ErrorCodes.getErrorCode(code);
    }
}
