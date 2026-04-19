package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.AddMemberRequest;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.service.WorkspaceService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Workspace> getAllWorkspaces() {
        return workspaceService.getAllWorkspaces();
    }

    @GetMapping(path = "/workspaces/{workspaceId}")
    public Workspace getWorkspaceById(@PathVariable UUID workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId);
    }

    @GetMapping(path = "/workspaces/{workspaceId}/members")
    public List<User> getWorkspaceMembers(@PathVariable UUID workspaceId) {
        return workspaceService.getUsersInWorkspace(workspaceId);
    }

    @PostMapping(path = "/workspaces")
    public Workspace createWorkspace(@RequestBody Workspace workspace) {
        return workspaceService.createWorkspace(workspace);
    }

    @PostMapping(path = "/workspaces/{workspaceId}/members")
    public void addWorkspaceMember(
        @PathVariable UUID workspaceId,
        @RequestBody AddMemberRequest request
    ) {
        workspaceService.addUserToWorkspace(
            workspaceId,
            request.getUserId(),
            request.getRole()
        );
    }

    @PutMapping(path = "/workspaces/{workspaceId}")
    public void updateWorkspace(
        @PathVariable UUID workspaceId,
        @RequestBody Workspace workspace
    ) {
        workspaceService.updateWorkspace(workspaceId, workspace);
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}")
    public void deleteWorkspace(@PathVariable UUID workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
    }

    @DeleteMapping(path = "/workspaces/{workspaceId}/members/{userId}")
    public void removeWorkspaceMember(
        @PathVariable UUID workspaceId,
        @PathVariable UUID userId
    ) {
        workspaceService.removeUserFromWorkspace(workspaceId, userId);
    }
}
