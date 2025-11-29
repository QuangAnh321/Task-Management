package com.example.task_management.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task_management.controllers.requests.TaskRequest;
import com.example.task_management.controllers.response.BasicResponse;
import com.example.task_management.controllers.response.BasicResponseWithData;
import com.example.task_management.models.Task;
import com.example.task_management.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @GetMapping
    public ResponseEntity<BasicResponseWithData<Task>> getAllTasks() {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BasicResponseWithData<Task>(true, "Tasks retrieved successfully", taskService.getAllTasks()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Task>> getTaskById(BigInteger id) {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BasicResponseWithData<Task>(true, "Task retrieved successfully", List.of(taskService.getTaskById(id))));
    }

    @PostMapping
    public ResponseEntity<BasicResponseWithData<Task>> createTask(@RequestBody TaskRequest request) {
        var newTask = taskService.createTask(
            request.name(),
            request.description(),
            request.parentTaskListId()
        );
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(new BasicResponseWithData<Task>(true, "Task created successfully", List.of(newTask)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Task>> updateTask(@RequestBody TaskRequest request, @PathVariable BigInteger id) {
        var updatedTask = taskService.updateTask(
            id,
            request.name(),
            request.description()
        );
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new BasicResponseWithData<Task>(true, "Task updated successfully", List.of(updatedTask)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> deleteTask(@PathVariable BigInteger id) {
        taskService.deleteTask(id);
        return ResponseEntity
            .status(HttpStatus.NO_CONTENT)
            .body(new BasicResponse(true, "Task deleted successfully"));
    }
}
