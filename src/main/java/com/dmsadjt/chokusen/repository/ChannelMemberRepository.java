package com.dmsadjt.chokusen.repository;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.ChannelMember;
import com.dmsadjt.chokusen.entity.ChannelMemberId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelMemberRepository
    extends JpaRepository<ChannelMember, ChannelMemberId>
{
    public List<ChannelMember> findByChannel(Channel channel);
}
