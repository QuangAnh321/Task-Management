package com.example.task_management.models;

import java.math.BigInteger;

import com.example.task_management.repositories.task.TaskRecord;

import lombok.Data;

@Data
public class Task {

    private BigInteger id;
    private String name;
    private String description;

    public Task(TaskRecord record) {
        this.id = record.getId();
        this.name = record.getName();
        this.description = record.getDescription();
    }
}
