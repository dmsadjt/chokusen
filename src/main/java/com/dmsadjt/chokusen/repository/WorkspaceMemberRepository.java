package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.entity.WorkspaceMember;
import com.dmsadjt.chokusen.entity.WorkspaceMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkspaceMemberRepository
    extends JpaRepository<WorkspaceMember, WorkspaceMemberId>
{
    public List<WorkspaceMember> findByWorkspace(Workspace workspace);
}
