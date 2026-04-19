package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.entity.WorkspaceMember;
import com.dmsadjt.chokusen.entity.WorkspaceMemberId;
import com.dmsadjt.chokusen.repository.UserRepository;
import com.dmsadjt.chokusen.repository.WorkspaceMemberRepository;
import com.dmsadjt.chokusen.repository.WorkspaceRepository;
import com.dmsadjt.chokusen.service.WorkspaceService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkspaceServiceImpl implements WorkspaceService {

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private WorkspaceMemberRepository workspaceMemberRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    @Override
    public Workspace getWorkspaceById(UUID id) {
        return workspaceRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));
    }

    @Override
    public Workspace createWorkspace(Workspace workspace) {
        return workspaceRepository.save(workspace);
    }

    @Override
    public Workspace updateWorkspace(UUID id, Workspace workspace) {
        Workspace existingWorkspace = workspaceRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        existingWorkspace.setWorkspaceName(workspace.getWorkspaceName());
        existingWorkspace.setUpdatedAt(LocalDateTime.now());
        return workspaceRepository.save(existingWorkspace);
    }

    @Override
    public void deleteWorkspace(UUID id) {
        workspaceRepository.deleteById(id);
    }

    @Override
    public List<User> getUsersInWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository
            .findById(workspaceId)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        List<WorkspaceMember> members =
            workspaceMemberRepository.findByWorkspace(workspace);
        return members
            .stream()
            .map(WorkspaceMember::getUser)
            .collect(Collectors.toList());
    }

    @Override
    public void addUserToWorkspace(UUID workspaceId, UUID userId, String role) {
        WorkspaceMember member = createMember(workspaceId, userId, role);
        workspaceMemberRepository.save(member);
    }

    private WorkspaceMember createMember(
        UUID workspaceId,
        UUID userId,
        String role
    ) {
        Workspace workspace = workspaceRepository
            .findById(workspaceId)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));

        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        WorkspaceMemberId id = new WorkspaceMemberId();
        id.setWorkspaceId(workspaceId);
        id.setUserId(userId);

        WorkspaceMember member = new WorkspaceMember();
        member.setId(id);
        member.setWorkspace(workspace);
        member.setUser(user);
        member.setDateJoined(LocalDateTime.now());
        member.setRole(role);
        return member;
    }

    @Override
    public void removeUserFromWorkspace(UUID workspaceId, UUID userId) {
        WorkspaceMember member = getMember(workspaceId, userId);
        workspaceMemberRepository.delete(member);
    }

    private WorkspaceMember getMember(UUID workspaceId, UUID userId) {
        WorkspaceMemberId id = new WorkspaceMemberId();
        id.setWorkspaceId(workspaceId);
        id.setUserId(userId);

        WorkspaceMember member = workspaceMemberRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Member not found"));
        return member;
    }
}
