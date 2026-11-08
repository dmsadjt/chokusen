package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.ChannelReadState;
import com.dmsadjt.chokusen.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelReadStateRepository
    extends JpaRepository<ChannelReadState, UUID>
{
    Optional<ChannelReadState> findByUserAndChannel(User user, Channel channel);

    List<ChannelReadState> findByChannel(Channel channel);
}
