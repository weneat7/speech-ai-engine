package com.cspl.common.gen_ai.speechaiengine.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class RestClientHelper {

    /**
     * RestClient instance for making HTTP requests
     */
    private final RestClient restClient;

    /**
     * ObjectMapper instance for JSON processing
     */
    private final ObjectMapper objectMapper;

    public Map<String,Object> call(String url , Object requestBody, Map<String,String> headers, MediaType contentType) throws JsonProcessingException {
        if(contentType.equals(MediaType.APPLICATION_FORM_URLENCODED)){
            Map<String,Object> request = objectMapper.convertValue(requestBody, new TypeReference<Map<String, Object>>() {});
            List<NameValuePair> params = new ArrayList<>();
            for (Map.Entry<String, Object> entry : request.entrySet()) {
                Object value = entry.getValue();
                if(value instanceof String){
                    params.add(new BasicNameValuePair(entry.getKey(), value.toString()));
                }else {
                    params.add(new BasicNameValuePair(entry.getKey(), objectMapper.writeValueAsString(value)));
                }
            }
            requestBody = URLEncodedUtils.format(params, StandardCharsets.UTF_8);
        }
        return objectMapper.convertValue(restClient
                .post()
                .uri(url)
                .headers(httpHeaders -> {
                    headers.forEach(httpHeaders::set);
                    httpHeaders.setContentType(contentType);
                })
                .body(requestBody)
                .retrieve()
                .body(Map.class), new TypeReference<Map<String, Object>>() {});
    }

    public Map<String,Object> call(String url , Map<String,String> headers){
        return objectMapper.convertValue(restClient
                .get()
                .uri(url)
                .headers(httpHeaders -> {
                    headers.forEach(httpHeaders::set);
                })
                .retrieve()
                .body(Map.class), new TypeReference<Map<String, Object>>() {});
    }
}
