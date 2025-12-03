package com.example.task_management.controllers.requests.task;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@JsonIgnoreProperties(ignoreUnknown = true)
public record TaskCreateRequest(
    @NotNull(message = "ID is required")
    @PositiveOrZero(message = "ID must be greater than or equal to zero")
    @JsonProperty("task_list_id")
    BigInteger taskListId,

    @NotBlank(message = "Name is required")
    String name,

    String description
) {
    
}
