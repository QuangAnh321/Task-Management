package com.example.task_management.controllers.requests.workspace;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;

@JsonIgnoreProperties(ignoreUnknown = true)
public record WorkspaceCreateRequest(
    @NotBlank(message = "Name is required")
    String name,
    String description
) {
    
}
