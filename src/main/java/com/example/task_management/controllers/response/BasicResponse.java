package com.example.task_management.controllers.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BasicResponse (
    boolean success,
    String message
) {
    
}
