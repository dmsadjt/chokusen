package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.Message;
import com.dmsadjt.chokusen.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    public List<Message> findByChannel(Channel channel);
    public List<Message> findByUser(User user);
}
