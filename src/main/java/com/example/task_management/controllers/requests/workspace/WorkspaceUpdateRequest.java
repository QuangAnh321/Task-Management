package com.example.task_management.controllers.requests.workspace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkspaceUpdateRequest(
    String name,
    String description
) {
    
}
