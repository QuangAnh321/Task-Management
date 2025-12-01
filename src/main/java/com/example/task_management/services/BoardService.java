package com.example.task_management.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task_management.models.Board;
import com.example.task_management.repositories.board.BoardRecord;
import com.example.task_management.repositories.board.BoardRepository;
import com.example.task_management.repositories.workspace.WorkspaceRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final WorkspaceRepository workspaceRepository;

    public BoardService(BoardRepository boardRepository, WorkspaceRepository workspaceRepository) {
        this.boardRepository = boardRepository;
        this.workspaceRepository = workspaceRepository;
    }

    public List<Board> getAll() {
        var allBoardRecords = boardRepository.findAll();
        return allBoardRecords.stream()
                .map(Board::new)
                .toList();
    }

    public Board getById(BigInteger id) {
        return boardRepository.findById(id)
                .map(Board::new)
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                MessageFormat.format("Board with id {0} not found", id)));
    }

    public List<Board> getAllByParentId(BigInteger workspaceId) {
        var boardRecords = boardRepository.findAllByParentWorkspaceId(workspaceId);
        return boardRecords.stream()
                .map(Board::new)
                .toList();
    }

    public Board create(String name, String description, BigInteger parentWorkspaceId) {
        var parentWorkSpace = workspaceRepository.findById(parentWorkspaceId);
        if (parentWorkSpace.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Workspace with id {0} not found when creating a new board", parentWorkspaceId));
        } else {
            var boardRecord = new BoardRecord();
            boardRecord.setName(name);
            boardRecord.setDescription(description);
            boardRecord.setParentWorkspace(parentWorkSpace.get());

            BoardRecord newBoardRecord = boardRepository.save(boardRecord);
            return new Board(newBoardRecord);
        }
    }

    // public Board update(BigInteger id, BigInteger parentWorkspaceId, String ownerEmail) {
    //     var boardRecordOpt = boardRepository.findById(id);
    //     if (boardRecordOpt.isPresent()) {
    //         var boardRecord = boardRecordOpt.get();
    //         if (isCurrentUserTheOwner(boardRecord, ownerEmail)) {
    //             var parentWorkSpace = workspaceRepository.findById(parentWorkspaceId);
    //             if (parentWorkSpace.isEmpty()) {
    //                 throw new EntityNotFoundException(
    //                         MessageFormat.format("updateBoard: Workspace with id {0} not found when updating board {1}", parentWorkspaceId, id));
    //             } else {
    //                 boardRecord.setParentWorkspace(parentWorkSpace.get());
    //                 var updatedWorkspaceRecord = boardRepository.save(boardRecord);
    //                 return new Board(updatedWorkspaceRecord);
    //             }
    //         } else {
    //             throw new SecurityException("updateBoard: You do not have permission to update this board");
    //         }
    //     } else {
    //         throw new EntityNotFoundException(MessageFormat.format("updateBoard: Board with id {0} not found", id));
    //     }
    // }

    public Board update(BigInteger id, String name, String description, String ownerEmail) {
        var boardRecordOpt = boardRepository.findById(id);
        if (boardRecordOpt.isPresent()) {
            var boardRecord = boardRecordOpt.get();
            if (isCurrentUserOwnThisResource(boardRecord, ownerEmail)) {
                var newName = name != null ? name : boardRecord.getName();
                var newDescription = description != null ? description : boardRecord.getDescription();
                boardRecord.setName(newName);
                boardRecord.setDescription(newDescription);
                var updatedWorkspaceRecord = boardRepository.save(boardRecord);
                return new Board(updatedWorkspaceRecord);
            } else {
                throw new SecurityException("You do not have permission to update this board");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id));
        }
    }

    public void deleteBoard(BigInteger id, String ownerEmail) {
        var boardTobeDeleted = boardRepository.findById(id);
        if (boardTobeDeleted.isPresent()) {
            if (isCurrentUserOwnThisResource(boardTobeDeleted.get(), ownerEmail)) {
                boardRepository.deleteById(id);
            } else {
                throw new SecurityException("updateBoard: You do not have permission to delete this board");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id));
        }
    }

    private boolean isCurrentUserOwnThisResource(BoardRecord boardRecord, String ownerEmail) {
        return boardRecord.getParentWorkspace().getOwnerEmail().equals(ownerEmail);
    }
}
