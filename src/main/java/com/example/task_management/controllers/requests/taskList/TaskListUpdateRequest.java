package com.example.task_management.controllers.requests.taskList;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskListUpdateRequest(
    String name,
    String description
) {
    
}
