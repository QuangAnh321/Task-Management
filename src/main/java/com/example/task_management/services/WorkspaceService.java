package com.example.task_management.services;

import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.task_management.models.Workspace;
import com.example.task_management.repositories.workspace.WorkspaceRecord;
import com.example.task_management.repositories.workspace.WorkspaceRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<Workspace> getAll() {
        var allWorkspaceRecords = workspaceRepository.findAll();
        return allWorkspaceRecords.stream()
                .map(Workspace::new)
                .toList();
    }

    public Workspace getById(BigInteger id) {
        return workspaceRepository.findById(id)
                .map(Workspace::new)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id)));
    }

    public Workspace create(String name, String description, String ownerEmail) {
        var workspaceRecord = new WorkspaceRecord();
        workspaceRecord.setName(name);
        workspaceRecord.setDescription(description);
        workspaceRecord.setOwnerEmail(ownerEmail);

        var newWorkspaceRecord = workspaceRepository.save(workspaceRecord);
        return new Workspace(newWorkspaceRecord);
    }

    public Workspace update(BigInteger id, String name, String description, String ownerEmail) {
        var workspaceRecordOpt = workspaceRepository.findById(id);
        if (workspaceRecordOpt.isPresent()) {
            var workspaceRecord = workspaceRecordOpt.get();
            if (workspaceRecord.getOwnerEmail().equals(ownerEmail)) {
                var newName = name != null ? name : workspaceRecord.getName();
                var newDescription = description != null ? description : workspaceRecord.getDescription();
                workspaceRecord.setName(newName);
                workspaceRecord.setDescription(newDescription);
                var updatedWorkspaceRecord = workspaceRepository.save(workspaceRecord);
                return new Workspace(updatedWorkspaceRecord);
            } else {
                throw new SecurityException("You do not have permission to update this workspace");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id));
        }
    }

    // Todo: Consider deleteing related boards, task lists and tasks when deleting a workspace
    public void deleteWorkspace(BigInteger id, String ownerEmail) {
        var workspaceTobeDeleted = workspaceRepository.findById(id);
        if (workspaceTobeDeleted.isPresent()) {
            if (workspaceTobeDeleted.get().getOwnerEmail().equals(ownerEmail)) {
                workspaceRepository.deleteById(id);
            } else {
                throw new SecurityException("You do not have permission to delete this workspace");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id));
        }
    }
}
