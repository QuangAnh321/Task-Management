package com.example.task_management.controllers;

import java.math.BigInteger;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.task_management.controllers.requests.task.TaskCreateRequest;
import com.example.task_management.controllers.requests.task.TaskUpdateRequest;
import com.example.task_management.controllers.response.BasicResponse;
import com.example.task_management.controllers.response.BasicResponseWithData;
import com.example.task_management.models.Task;
import com.example.task_management.services.TaskService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/task-management")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    
    @GetMapping("/tasks")
    public ResponseEntity<BasicResponseWithData<Task>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Task>(true, "Task retrieved successfully",
                        taskService.getAll()));
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<BasicResponseWithData<Task>> getById(@PathVariable("id") BigInteger id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Task>(true, "Task retrieved successfully",
                        List.of(taskService.getById(id))));
    }

    @GetMapping("/task-lists/{taskListId}/tasks")
    public ResponseEntity<BasicResponseWithData<Task>> getAllByParentId(@PathVariable("taskListId") BigInteger taskListId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Task>(true, "Task retrieved successfully",
                        taskService.getAllByParentId(taskListId)));
    }

    @PostMapping("/tasks")
    public ResponseEntity<BasicResponseWithData<Task>> create(@RequestBody @Valid TaskCreateRequest request) {
        var newTask = taskService.create(
                request.name(),
                request.description(),
                request.taskListId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BasicResponseWithData<Task>(true, "Task created successfully",
                        List.of(newTask)));
    }

    @PutMapping("/tasks/{id}")
    public ResponseEntity<BasicResponseWithData<Task>> update(
            @RequestBody @Valid TaskUpdateRequest request, @PathVariable("id") BigInteger id,
            @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        var updatedTask = taskService.update(
                id,
                request.name(),
                request.description(),
                request.taskListId(),
                userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Task>(true, "Task updated successfully",
                        List.of(updatedTask)));
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable("id") BigInteger id, @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        taskService.delete(id, userEmail);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new BasicResponse(true, "Task list deleted successfully"));
    }
}
