package com.cspl.common.gen_ai.speechaiengine.models.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Builder
@AllArgsConstructor
@Data
@NoArgsConstructor
public class Transcription {
    private String role;
    private String content;
    private String contentType;
    private long contentTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Map<String,Object> metaData;
}
