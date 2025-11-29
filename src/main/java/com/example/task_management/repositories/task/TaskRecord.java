package com.example.task_management.repositories.task;

import java.math.BigInteger;

import com.example.task_management.repositories.taskList.TaskListRecord;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
@Table(name = "tasks")
public class TaskRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "task_list_id", nullable = false)
    private TaskListRecord parentTaskList;
}
