package com.example.task_management.repositories.taskList;

import java.math.BigInteger;
import java.util.Set;

import com.example.task_management.models.TaskList;
import com.example.task_management.repositories.board.BoardRecord;
import com.example.task_management.repositories.task.TaskRecord;

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
@Table(name = "task_lists")
public class TaskListRecord {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private BigInteger id;
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private BoardRecord parentBoard;

    @OneToMany(mappedBy = "parentTaskList")
    private Set<TaskRecord> tasks;

    public TaskListRecord(TaskList taskList, BoardRecord parentBoard) {
        this.name = taskList.getName();
        this.description = taskList.getDescription();
        this.parentBoard = parentBoard;
    }
}
