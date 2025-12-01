package com.example.task_management.controllers.requests.board;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BoardUpdateRequest(
    String name,
    String description
) {
    
}
