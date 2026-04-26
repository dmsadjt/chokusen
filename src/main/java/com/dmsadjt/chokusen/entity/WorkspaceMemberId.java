package com.dmsadjt.chokusen.entity;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
public class WorkspaceMemberId implements Serializable {

    private UUID userId;
    private UUID workspaceId;
}
