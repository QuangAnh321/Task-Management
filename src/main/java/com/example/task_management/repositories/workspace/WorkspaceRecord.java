package com.example.task_management.repositories.workspace;

import java.math.BigInteger;
import java.util.Set;

import com.example.task_management.repositories.board.BoardRecord;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "workspaces")
@Data
@NoArgsConstructor
public class WorkspaceRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private String description;
    private String ownerEmail;

    @OneToMany(mappedBy = "parentWorkspace")
    private Set<BoardRecord> boards;
}
