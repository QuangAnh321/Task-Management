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

    public List<Task> getAll() {
        var allTaskRecords = taskRepository.findAll();
        return allTaskRecords.stream()
                .map(Task::new)
                .toList();
    }

    public Task getById(BigInteger id) {
        return taskRepository.findById(id)
                .map(Task::new)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Task with id {0} not found", id)));
    }

    public List<Task> getAllByParentId(BigInteger taskListId) {
        var parentTaskList = taskListRepository.findById(taskListId);
        if (parentTaskList.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Task list with id {0} not found when getting tasks", taskListId));
        } else {
            var taskListRecords = taskRepository.findAllByParentTaskListId(taskListId);
            return taskListRecords.stream()
                .map(Task::new)
                .toList();
        }
    }

    public Task create(String name, String description, BigInteger parentTaskListId) {
        var parentTaskList = taskListRepository.findById(parentTaskListId);
        if (parentTaskList.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Task list with id {0} not found when creating a new task", parentTaskListId));
        } else {
            var taskRecord = new TaskRecord();
            taskRecord.setName(name);
            taskRecord.setDescription(description);
            taskRecord.setParentTaskList(parentTaskList.get());

            TaskRecord newTaskRecord = taskRepository.save(taskRecord);
            return new Task(newTaskRecord);
        }
    }

    public Task update(BigInteger id, String name, String description, BigInteger parentTaskListId, String ownerEmail) {
        System.out.println("Parent Task List ID: " + parentTaskListId);
        var taskRecordOpt = taskRepository.findById(id);
        if (taskRecordOpt.isPresent()) {
            var taskRecord = taskRecordOpt.get();
            if (isCurrentUserOwnThisResource(taskRecord, ownerEmail)) {
                var newName = name != null ? name : taskRecord.getName();
                var newDescription = description != null ? description : taskRecord.getDescription();
                var newTaskListParent = parentTaskListId != null
                        ? taskListRepository.findById(parentTaskListId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                        MessageFormat.format("Task list with id {0} not found when updating task list", parentTaskListId)))
                        : taskRecord.getParentTaskList();
                
                taskRecord.setName(newName);
                taskRecord.setDescription(newDescription);
                taskRecord.setParentTaskList(newTaskListParent);
                var updatedTaskRecord = taskRepository.save(taskRecord);
                return new Task(updatedTaskRecord);
            } else {
                throw new SecurityException("You do not have permission to update this task");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task with id {0} not found", id));
        }
    }

    public void delete(BigInteger id, String ownerEmail) {
        var taskTobeDeleted = taskRepository.findById(id);
        if (taskTobeDeleted.isPresent()) {
            if (isCurrentUserOwnThisResource(taskTobeDeleted.get(), ownerEmail)) {
                taskRepository.deleteById(id);
            } else {
                throw new SecurityException("You do not have permission to delete this task");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task with id {0} not found", id));
        }
    }

    private boolean isCurrentUserOwnThisResource(TaskRecord taskRecord, String ownerEmail) {
        return taskRecord
            .getParentTaskList()
            .getParentBoard()
            .getParentWorkspace().getOwnerEmail().equals(ownerEmail);
    }
}
