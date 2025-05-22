package com.cspl.common.gen_ai.speechaiengine.controller;


import com.cspl.common.gen_ai.speechaiengine.constants.APIConstants;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author vineet.rajput
 * description: class contain the health based urls
 */
@RestController
public class PublicHealthController {

    /**
     * Health API
     * @return
     */
    @GetMapping(value = APIConstants.HEALTH_URL)
    public ResponseEntity<String> health(){
        return ResponseEntity.ok("Ok Health");
    }
}
