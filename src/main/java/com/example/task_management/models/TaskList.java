package com.example.task_management.models;

import java.math.BigInteger;

import com.example.task_management.repositories.taskList.TaskListRecord;

import lombok.Data;

@Data
public class TaskList {
    
    private BigInteger id;
    private String name;
    private String description;
    private Board board;

    public TaskList(TaskListRecord record) {
        this.id = record.getId();
        this.name = record.getName();
        this.description = record.getDescription();
        this.board = new Board(record.getParentBoard());
    }
}
