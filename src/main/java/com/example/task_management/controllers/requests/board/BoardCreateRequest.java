package com.example.task_management.controllers.requests.board;

import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@JsonIgnoreProperties(ignoreUnknown = true)
public record BoardCreateRequest(
    @NotNull(message = "ID is required")
    @PositiveOrZero(message = "ID must be greater than or equal to zero")
    @JsonProperty("workspace_id")
    BigInteger workspaceId,

    @NotBlank(message = "Name is required")
    String name,

    String description
) {
    
}
