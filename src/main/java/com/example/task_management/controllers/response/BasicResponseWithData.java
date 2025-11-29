package com.example.task_management.controllers.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BasicResponseWithData <T> (
    boolean success,
    String message,
    List<T> data
) {
    
}
