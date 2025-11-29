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

import com.example.task_management.controllers.requests.workspace.WorkspaceCreateRequest;
import com.example.task_management.controllers.requests.workspace.WorkspaceUpdateRequest;
import com.example.task_management.controllers.response.BasicResponse;
import com.example.task_management.controllers.response.BasicResponseWithData;
import com.example.task_management.models.Workspace;
import com.example.task_management.services.WorkspaceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/task-management/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    public WorkspaceController(WorkspaceService workspaceService) {
        this.workspaceService = workspaceService;
    }

    @GetMapping
    public ResponseEntity<BasicResponseWithData<Workspace>> getAllWorkspaces() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Workspace>(true, "Workspaces retrieved successfully",
                        workspaceService.getAllWorkspaces()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Workspace>> getWorkspaceById(@PathVariable BigInteger id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Workspace>(true, "Workspace retrieved successfully",
                        List.of(workspaceService.getWorkspaceById(id))));
    }

    @PostMapping
    public ResponseEntity<BasicResponseWithData<Workspace>> createWorkspace(
            @RequestBody @Valid WorkspaceCreateRequest request, @AuthenticationPrincipal OidcUser oidcUser) {
        var userEmail = oidcUser.getEmail();
        var newWorkspace = workspaceService.createWorkspace(
                request.name(),
                request.description(),
                userEmail);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BasicResponseWithData<Workspace>(true, "Workspace created successfully",
                        List.of(newWorkspace)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Workspace>> updateWorkspace(
            @RequestBody @Valid WorkspaceUpdateRequest request, @PathVariable("id") BigInteger id,
            @AuthenticationPrincipal OidcUser oidcUser) {
        var userEmail = oidcUser.getEmail();
        var updatedWorkspace = workspaceService.updateWorkspace(
                id,
                request.name(),
                request.description(),
                userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Workspace>(true, "Workspace updated successfully",
                        List.of(updatedWorkspace)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> deleteWorkspace(@PathVariable("id") BigInteger id, @AuthenticationPrincipal OidcUser oidcUser) {
        var userEmail = oidcUser.getEmail();
        workspaceService.deleteWorkspace(id, userEmail);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new BasicResponse(true, "Workspace deleted successfully"));
    }
}
