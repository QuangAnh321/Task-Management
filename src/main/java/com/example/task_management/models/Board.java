package com.example.task_management.models;

import java.math.BigInteger;

import com.example.task_management.repositories.board.BoardRecord;

import lombok.Data;

@Data
public class Board {
    
    private BigInteger id;
    private String name;
    private String description;
    private Workspace workspace;

    public Board(BoardRecord record) {
        this.id = record.getId();
        this.name = record.getName();
        this.description = record.getDescription();
        this.workspace = new Workspace(record.getParentWorkspace());
    }
}
