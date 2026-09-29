package com.example.task_management.controllers.requests.task;

import java.math.BigInteger;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskStreamRequest(
    @NotNull(message = "Task list IDs cannot be null")
    @JsonProperty("task_list_ids")
    List<BigInteger> taskListIds
) {
    
}
