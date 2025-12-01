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

import com.example.task_management.controllers.requests.board.BoardCreateRequest;
import com.example.task_management.controllers.requests.board.BoardUpdateRequest;
import com.example.task_management.controllers.response.BasicResponse;
import com.example.task_management.controllers.response.BasicResponseWithData;
import com.example.task_management.models.Board;
import com.example.task_management.services.BoardService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/task-management/boards")
public class BoardController {
    
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping
    public ResponseEntity<BasicResponseWithData<Board>> getAll() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Board>(true, "Boards retrieved successfully",
                        boardService.getAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Board>> getById(@PathVariable("id") BigInteger id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Board>(true, "Board retrieved successfully",
                        List.of(boardService.getById(id))));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<BasicResponseWithData<Board>> getAllByParentId(@PathVariable("workspaceId") BigInteger workspaceId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Board>(true, "Boards retrieved successfully",
                        boardService.getAllByParentId(workspaceId)));
    }

    @PostMapping
    public ResponseEntity<BasicResponseWithData<Board>> create(@RequestBody @Valid BoardCreateRequest request) {
        var newBoard = boardService.create(
                request.name(),
                request.description(),
                request.workspaceId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new BasicResponseWithData<Board>(true, "Board created successfully",
                        List.of(newBoard)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BasicResponseWithData<Board>> update(
            @RequestBody @Valid BoardUpdateRequest request, @PathVariable("id") BigInteger id,
            @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        var updatedBoard = boardService.update(
                id,
                request.name(),
                request.description(),
                userEmail);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new BasicResponseWithData<Board>(true, "Board updated successfully",
                        List.of(updatedBoard)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BasicResponse> delete(@PathVariable("id") BigInteger id, @AuthenticationPrincipal OidcUser currentUser) {
        var userEmail = currentUser.getEmail();
        boardService.deleteBoard(id, userEmail);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new BasicResponse(true, "Board deleted successfully"));
    }
}
