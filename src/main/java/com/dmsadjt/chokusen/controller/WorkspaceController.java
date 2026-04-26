package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.AddMemberRequest;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.service.WorkspaceService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/api/v1")
public class WorkspaceController {

    @Autowired
    private WorkspaceService workspaceService;

    @GetMapping(path = "/workspaces")
    public ResponseEntity<List<Workspace>> getAllWorkspaces() {
        return ResponseEntity.ok(workspaceService.getAllWorkspaces());
    }

    @GetMapping(path = "/workspaces/{workspaceId}")
    public ResponseEntity<Workspace> getWorkspaceById(
        @PathVariable UUID workspaceId
    ) {
        return ResponseEntity.ok(
            workspaceService.getWorkspaceById(workspaceId)
        );
    }

    @GetMapping(path = "/workspaces/{workspaceId}/members")
    public ResponseEntity<List<User>> getWorkspaceMembers(
        @PathVariable UUID workspaceId
    ) {
        return ResponseEntity.ok(
            workspaceService.getUsersInWorkspace(workspaceId)
        );
    }

    @PostMapping(path = "/workspaces")
    public ResponseEntity<Workspace> createWorkspace(
        @RequestBody Workspace workspace
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            workspaceService.createWorkspace(workspace)
        );
    }

    @PostMapping(path = "/workspaces/{workspaceId}/members")
    public ResponseEntity<Void> addWorkspaceMember(
        @PathVariable UUID workspaceId,
        @RequestBody AddMemberRequest request
    ) {
        workspaceService.addUserToWorkspace(
            workspaceId,
            request.getUserId(),
            request.getRole()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/workspaces/{workspaceId}")
    public ResponseEntity<Void> updateWorkspace(
        @PathVariable UUID workspaceId,
        @RequestBody Workspace workspace
    ) {
        workspaceService.updateWorkspace(workspaceId, workspace);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
        @PathVariable UUID workspaceId
    ) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}/members/{userId}")
    public ResponseEntity<Void> removeWorkspaceMember(
        @PathVariable UUID workspaceId,
        @PathVariable UUID userId
    ) {
        workspaceService.removeUserFromWorkspace(workspaceId, userId);
        return ResponseEntity.noContent().build();
    }
}
