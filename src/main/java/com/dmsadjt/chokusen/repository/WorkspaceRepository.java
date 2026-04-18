package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Workspace;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceRepository extends JpaRepository<Workspace, UUID> {}
