package com.example.task_management.repositories.taskList;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskListRepository extends JpaRepository<TaskListRecord, BigInteger> {
    
}
