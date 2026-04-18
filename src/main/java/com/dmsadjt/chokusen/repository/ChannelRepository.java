package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.Workspace;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {
    public List<Channel> findChannelByWorkspace(Workspace workspace);
}
