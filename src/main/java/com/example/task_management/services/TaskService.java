package com.example.task_management.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task_management.models.Task;
import com.example.task_management.repositories.task.TaskRecord;
import com.example.task_management.repositories.task.TaskRepository;
import com.example.task_management.repositories.taskList.TaskListRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {
    
    private final TaskRepository taskRepository;
    private final TaskListRepository taskListRepository;

    public TaskService(TaskRepository taskRepository, TaskListRepository taskListRepository) {
        this.taskRepository = taskRepository;
        this.taskListRepository = taskListRepository;
    }

    public List<Task> getAllTasks() {
        var allTaskRecords = taskRepository.findAll();
        return allTaskRecords.stream()
            .map(Task::new)
            .toList();
    }

    public Task getTaskById(BigInteger id) {
        return taskRepository.findById(id)
            .map(Task::new)
            .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Task with id {0} not found", id)));
    }

    public Task createTask(String name, String description, BigInteger parentTaskListId) {
        var parentTaskList = taskListRepository.findById(parentTaskListId); // Fetch TaskListRecord by parentTaskListId
        if (parentTaskList.isPresent()) {
            var taskRecord = new TaskRecord();
            taskRecord.setName(name);
            taskRecord.setDescription(description);
            taskRecord.setParentTaskList(parentTaskList.get());
            TaskRecord newTaskRecord = taskRepository.save(taskRecord);
            return new Task(newTaskRecord);
        } else {
            throw new EntityNotFoundException(MessageFormat.format("TaskList with id {0} not found", parentTaskListId));
        }
    }

    public Task updateTask(BigInteger id, String name, String description) {
        var taskToBeUpdated = taskRepository.findById(id);
        if (taskToBeUpdated.isPresent()) {
            var taskRecord = taskToBeUpdated.get();
            taskRecord.setName(name);
            taskRecord.setDescription(description);
            TaskRecord updatedTaskRecord = taskRepository.save(taskRecord);
            return new Task(updatedTaskRecord);
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task with id {0} not found", id));
        }
    }

    public void deleteTask(BigInteger id) {
        var taskTobeDeleted = taskRepository.findById(id);
        if (taskTobeDeleted.isPresent()) {
            taskRepository.deleteById(id);
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task with id {0} not found", id));
        }
    }
}
