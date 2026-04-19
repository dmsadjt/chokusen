package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import java.util.List;
import java.util.UUID;

public interface WorkspaceService {
    List<Workspace> getAllWorkspaces();
    Workspace getWorkspaceById(UUID id);
    Workspace createWorkspace(Workspace workspace);
    Workspace updateWorkspace(UUID id, Workspace workspace);
    void deleteWorkspace(UUID id);
    List<User> getUsersInWorkspace(UUID workspaceId);
    void addUserToWorkspace(UUID workspaceId, UUID userId, String role);
    void removeUserFromWorkspace(UUID workspaceId, UUID userId);
}
