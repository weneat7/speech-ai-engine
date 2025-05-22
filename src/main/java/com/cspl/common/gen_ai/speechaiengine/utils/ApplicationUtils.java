package com.cspl.common.gen_ai.speechaiengine.utils;

import com.cspl.common.gen_ai.speechaiengine.config.AppProperties;
import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import com.cspl.common.gen_ai.speechaiengine.exceptions.CustomAuthException;
import com.cspl.common.gen_ai.speechaiengine.models.enums.ErrorCodes;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@AllArgsConstructor
public class ApplicationUtils {

    private final AppProperties.SAEHeaderConfig saeHeaderConfig;

    public  boolean validateApiHeader(Map<String,String> headers, String requestKey){
      String validateKey = null;

      switch (requestKey){
          case APIConstants.REQUEST_KEYS.AUTH_KEY -> validateKey = saeHeaderConfig.getAuthApiKey();
          case APIConstants.REQUEST_KEYS.CACHE_KEY -> validateKey = saeHeaderConfig.getCacheApiKey();
          case APIConstants.REQUEST_KEYS.UPDATE_KEY -> validateKey = saeHeaderConfig.getConfigApiKey();
          default -> throw new CustomAuthException(ErrorCodes.UNAUTHORIZED.getCode(),HttpStatus.UNAUTHORIZED);
      }

        if(Objects.nonNull(validateKey)
                && !CollectionUtils.isEmpty(headers)
                && headers.containsKey(APIConstants.HEADERS.X_API_KEY)
                && headers.get(APIConstants.HEADERS.X_API_KEY).equals(validateKey)){
            return true;
        }
        throw new CustomAuthException(ErrorCodes.UNAUTHORIZED.getCode(),  HttpStatus.UNAUTHORIZED);
    }
}
