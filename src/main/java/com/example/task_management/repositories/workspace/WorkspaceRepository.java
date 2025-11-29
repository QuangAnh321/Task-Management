package com.example.task_management.repositories.workspace;

import java.math.BigInteger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<WorkspaceRecord, BigInteger> {
    
}
