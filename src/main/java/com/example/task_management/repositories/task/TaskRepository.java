package com.example.task_management.repositories.task;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<TaskRecord, BigInteger> {
    
}
