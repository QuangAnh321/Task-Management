package com.example.task_management.repositories.board;

import java.math.BigInteger;
import java.util.Set;

import com.example.task_management.repositories.taskList.TaskListRecord;
import com.example.task_management.repositories.workspace.WorkspaceRecord;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class BoardRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceRecord parentWorkspace;

    @OneToMany(mappedBy = "parentBoard")
    private Set<TaskListRecord> taskLists;

    public BoardRecord(String name, String description, WorkspaceRecord parentWorkspace) {
        this.name = name;
        this.description = description;
        this.parentWorkspace = parentWorkspace;
    }
}
