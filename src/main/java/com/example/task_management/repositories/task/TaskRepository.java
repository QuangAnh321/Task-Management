package com.example.task_management.repositories.task;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskRecord, BigInteger> {
    
     List<TaskRecord> findAllByParentTaskListId(BigInteger taskId);
}
