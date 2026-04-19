package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.ChannelMember;
import com.dmsadjt.chokusen.entity.ChannelMemberId;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.repository.ChannelMemberRepository;
import com.dmsadjt.chokusen.repository.ChannelRepository;
import com.dmsadjt.chokusen.repository.UserRepository;
import com.dmsadjt.chokusen.repository.WorkspaceRepository;
import com.dmsadjt.chokusen.service.ChannelService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChannelServiceImpl implements ChannelService {

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private ChannelMemberRepository channelMemberRepository;

    @Autowired
    private WorkspaceRepository workspaceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public Channel createChannel(Channel channel) {
        return channelRepository.save(channel);
    }

    @Override
    public Channel updateChannel(UUID id, Channel channel) {
        Channel existingChannel = channelRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
        existingChannel.setChannelName(channel.getChannelName());
        existingChannel.setPrivate(channel.isPrivate());
        existingChannel.setUpdatedAt(LocalDateTime.now());
        return channelRepository.save(existingChannel);
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel existingChannel = channelRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
        channelRepository.delete(existingChannel);
    }

    @Override
    public List<Channel> getAllChannels() {
        return channelRepository.findAll();
    }

    @Override
    public List<Channel> getChannelsInWorkspace(UUID workspaceId) {
        Workspace workspace = workspaceRepository
            .findById(workspaceId)
            .orElseThrow(() -> new RuntimeException("Workspace not found"));
        return channelRepository.findChannelByWorkspace(workspace);
    }

    @Override
    public List<User> getUsersInChannel(UUID channelId) {
        Channel channel = channelRepository
            .findById(channelId)
            .orElseThrow(() -> new RuntimeException("Channel not found"));

        List<ChannelMember> channelMembers =
            channelMemberRepository.findByChannel(channel);
        return channelMembers.stream().map(ChannelMember::getUser).toList();
    }

    @Override
    public void joinChannel(UUID channelId, UUID userId) {
        Channel channel = channelRepository
            .findById(channelId)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
        User user = userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        ChannelMemberId id = new ChannelMemberId();
        id.setChannelId(channelId);
        id.setUserId(userId);

        ChannelMember channelMember = new ChannelMember();
        channelMember.setId(id);
        channelMember.setChannel(channel);
        channelMember.setUser(user);
        channelMember.setDateJoined(LocalDateTime.now());
        channelMemberRepository.save(channelMember);
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        ChannelMemberId id = new ChannelMemberId();
        id.setChannelId(channelId);
        id.setUserId(userId);

        ChannelMember channelMember = channelMemberRepository
            .findById(id)
            .orElseThrow(() ->
                new RuntimeException("Channel member not found")
            );
        channelMemberRepository.delete(channelMember);
    }

    @Override
    public Channel getChannelById(UUID id) {
        return channelRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
    }
}
