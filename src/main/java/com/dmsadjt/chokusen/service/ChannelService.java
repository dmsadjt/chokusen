package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.User;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel createChannel(Channel channel);
    Channel updateChannel(UUID id, Channel channel);
    Channel getChannelById(UUID id);
    void deleteChannel(UUID id);
    List<Channel> getAllChannels();
    List<Channel> getChannelsInWorkspace(UUID workspaceId);
    List<User> getUsersInChannel(UUID channelId);
    void joinChannel(UUID channelId, UUID userId);
    void leaveChannel(UUID channelId, UUID userId);
}
