package com.example.task_management.controllers.requests;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkspaceRequest(
    String name,
    String description
) {
    
}
