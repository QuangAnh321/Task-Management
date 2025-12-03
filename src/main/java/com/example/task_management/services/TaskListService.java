package com.example.task_management.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task_management.models.TaskList;
import com.example.task_management.repositories.board.BoardRepository;
import com.example.task_management.repositories.taskList.TaskListRecord;
import com.example.task_management.repositories.taskList.TaskListRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskListService {
    
    private final BoardRepository boardRepository;
    private final TaskListRepository taskListRepository;

    public TaskListService(BoardRepository boardRepository, TaskListRepository taskListRepository) {
        this.boardRepository = boardRepository;
        this.taskListRepository = taskListRepository;
    }

    public List<TaskList> getAll() {
        var allTaskListRecords = taskListRepository.findAll();
        return allTaskListRecords.stream()
                .map(TaskList::new)
                .toList();
    }

    public TaskList getById(BigInteger id) {
        return taskListRepository.findById(id)
                .map(TaskList::new)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Task list with id {0} not found", id)));
    }

    public List<TaskList> getAllByParentId(BigInteger boardId) {
        var parentBoard = boardRepository.findById(boardId);
        if (parentBoard.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Board with id {0} not found when getting task lists", boardId));
        } else {
            var boardRecords = taskListRepository.findAllByParentBoardId(boardId);
            return boardRecords.stream()
                .map(TaskList::new)
                .toList();
        }
    }

    public TaskList create(String name, String description, BigInteger parentBoardId) {
        var parentBoard = boardRepository.findById(parentBoardId);
        if (parentBoard.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Board with id {0} not found when creating a new task list", parentBoardId));
        } else {
            var taskListRecord = new TaskListRecord();
            taskListRecord.setName(name);
            taskListRecord.setDescription(description);
            taskListRecord.setParentBoard(parentBoard.get());

            TaskListRecord newTaskListRecord = taskListRepository.save(taskListRecord);
            return new TaskList(newTaskListRecord);
        }
    }

    public TaskList update(BigInteger id, String name, String description, String ownerEmail) {
        var taskListRecordOpt = taskListRepository.findById(id);
        if (taskListRecordOpt.isPresent()) {
            var taskListRecord = taskListRecordOpt.get();
            if (isCurrentUserOwnThisResource(taskListRecord, ownerEmail)) {
                var newName = name != null ? name : taskListRecord.getName();
                var newDescription = description != null ? description : taskListRecord.getDescription();
                taskListRecord.setName(newName);
                taskListRecord.setDescription(newDescription);
                var updatedTaskListRecord = taskListRepository.save(taskListRecord);
                return new TaskList(updatedTaskListRecord);
            } else {
                throw new SecurityException("You do not have permission to update this task list");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task list with id {0} not found", id));
        }
    }

    public void delete(BigInteger id, String ownerEmail) {
        var taskListTobeDeleted = taskListRepository.findById(id);
        if (taskListTobeDeleted.isPresent()) {
            if (isCurrentUserOwnThisResource(taskListTobeDeleted.get(), ownerEmail)) {
                taskListRepository.deleteById(id);
            } else {
                throw new SecurityException("You do not have permission to delete this task list");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Task list with id {0} not found", id));
        }
    }

    private boolean isCurrentUserOwnThisResource(TaskListRecord taskListRecord, String ownerEmail) {
        return taskListRecord
            .getParentBoard()
            .getParentWorkspace().getOwnerEmail().equals(ownerEmail);
    }
}
