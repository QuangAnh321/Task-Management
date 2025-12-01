package com.example.task_management.repositories.board;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<BoardRecord, BigInteger> {
    
    List<BoardRecord> findAllByParentWorkspaceId(BigInteger workspaceId);
}
