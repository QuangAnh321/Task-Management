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

import com.example.task_management.controllers.requests.taskList.TaskListCreateRequest;
import com.example.task_management.controllers.requests.taskList.TaskListUpdateRequest;
import com.example.task_management.controllers.response.BasicResponse;
import com.example.task_management.controllers.response.BasicResponseWithData;
import com.example.task_management.models.TaskList;
import com.example.task_management.services.TaskListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/task-management")
public class TaskListController {
    
    private final TaskListService taskListService;

    public TaskListController(TaskListService taskListService) {
        this.taskListService = taskListService;
    }

    @GetMapping("/task-lists")
    public ResponseEntity<BasicResponseWithData<TaskList>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<TaskList>(true, "Task lists retrieved successfully",
                        taskListService.getAll()));
    }

    @GetMapping("/task-lists/{id}")
    public ResponseEntity<BasicResponseWithData<TaskList>> getById(@PathVariable("id") BigInteger id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<TaskList>(true, "Task list retrieved successfully",
                        List.of(taskListService.getById(id))));
    }

    @GetMapping("/boards/{boardId}/task-lists")
    public ResponseEntity<BasicResponseWithData<TaskList>> getAllByParentId(@PathVariable("boardId") BigInteger boardId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<TaskList>(true, "Task lists retrieved successfully",
                        taskListService.getAllByParentId(boardId)));
    }

    @PostMapping("/task-lists")
    public ResponseEntity<BasicResponseWithData<TaskList>> create(@RequestBody @Valid TaskListCreateRequest request) {
        var newTaskList = taskListService.create(
                request.name(),
                request.description(),
                request.boardId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BasicResponseWithData<TaskList>(true, "Task list created successfully",
                        List.of(newTaskList)));
    }

    @PutMapping("/task-lists/{id}")
    public ResponseEntity<BasicResponseWithData<TaskList>> update(
            @RequestBody @Valid TaskListUpdateRequest request, @PathVariable("id") BigInteger id,
            @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        var updatedTaskList = taskListService.update(
                id,
                request.name(),
                request.description(),
                userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<TaskList>(true, "Task list updated successfully",
                        List.of(updatedTaskList)));
    }

    @DeleteMapping("/task-lists/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable("id") BigInteger id, @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        taskListService.delete(id, userEmail);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new BasicResponse(true, "Task list deleted successfully"));
    }
}
