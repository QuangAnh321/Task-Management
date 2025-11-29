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

    public List<Workspace> getAllWorkspaces() {
        var allWorkspaceRecords = workspaceRepository.findAll();
        return allWorkspaceRecords.stream()
                .map(Workspace::new)
                .toList();
    }

    public Workspace getWorkspaceById(BigInteger id) {
        return workspaceRepository.findById(id)
                .map(Workspace::new)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id)));
    }

    public Workspace createWorkspace(String name, String description, String ownerEmail) {
        var workspaceRecord = new WorkspaceRecord();
        workspaceRecord.setName(name);
        workspaceRecord.setDescription(description);
        workspaceRecord.setOwnerEmail(ownerEmail);

        WorkspaceRecord newWorkspaceRecord = workspaceRepository.save(workspaceRecord);
        return new Workspace(newWorkspaceRecord);
    }

    public Workspace updateWorkspace(BigInteger id, String name, String description, String ownerEmail) {
        var workspaceToBeUpdated = workspaceRepository.findById(id);
        if (workspaceToBeUpdated.isPresent()) {
            var workspaceRecord = workspaceToBeUpdated.get();
            if (workspaceRecord.getOwnerEmail().equals(ownerEmail)) {
                var newName = name != null ? name : workspaceRecord.getName();
                var newDescription = description != null ? description : workspaceRecord.getDescription();
                workspaceRecord.setName(newName);
                workspaceRecord.setDescription(newDescription);
                WorkspaceRecord updatedWorkspaceRecord = workspaceRepository.save(workspaceRecord);
                return new Workspace(updatedWorkspaceRecord);
            } else {
                throw new SecurityException("You do not have permission to update this workspace");
            }
        } else {
            throw new EntityNotFoundException(MessageFormat.format("Workspace with id {0} not found", id));
        }
    }

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
