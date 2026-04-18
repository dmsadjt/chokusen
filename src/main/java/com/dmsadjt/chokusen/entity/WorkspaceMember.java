package com.dmsadjt.chokusen.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workspace_members")
@Getter
@Setter
@NoArgsConstructor
public class WorkspaceMember {

    @EmbeddedId
    private WorkspaceMemberId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("workspaceId")
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    private LocalDateTime dateJoined;
    private String role;
}
