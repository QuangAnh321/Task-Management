package com.example.task_management.models;

import java.math.BigInteger;

import com.example.task_management.repositories.workspace.WorkspaceRecord;

import lombok.Data;

@Data
public class Workspace {
    
    private BigInteger id;
    private String name;
    private String description;
    private String ownerEmail;

    public Workspace(WorkspaceRecord record) {
        this.id = record.getId();
        this.name = record.getName();
        this.description = record.getDescription();
        this.ownerEmail = record.getOwnerEmail();
    }
}
