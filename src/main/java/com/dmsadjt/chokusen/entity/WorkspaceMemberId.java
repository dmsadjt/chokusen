package com.dmsadjt.chokusen.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class WorkspaceMemberId implements Serializable {

    private UUID userId;
    private UUID workspaceId;
}
