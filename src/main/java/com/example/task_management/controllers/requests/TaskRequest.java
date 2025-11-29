package com.example.task_management.controllers.requests;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskRequest(
    String name,
    String description,
    BigInteger parentTaskListId
) {
    
}
