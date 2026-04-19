package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.AddChannelMemberRequest;
import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.service.ChannelService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @GetMapping(path = "/channels")
    public List<Channel> getAllChannels() {
        return channelService.getAllChannels();
    }

    @GetMapping(path = "/channels/{channelId}")
    public Channel getChannelById(@PathVariable UUID channelId) {
        return channelService.getChannelById(channelId);
    }

    @GetMapping(path = "/workspaces/{workspaceId}/channels")
    public List<Channel> getChannelsInWorkspace(
        @PathVariable UUID workspaceId
    ) {
        return channelService.getChannelsInWorkspace(workspaceId);
    }

    @GetMapping(path = "/channels/{channelId}/members")
    public List<User> getMembersInChannel(@PathVariable UUID channelId) {
        return channelService.getUsersInChannel(channelId);
    }

    @PostMapping(path = "/channels")
    public Channel createChannel(@RequestBody Channel channel) {
        return channelService.createChannel(channel);
    }

    @PostMapping(path = "/channels/{channelId}/members")
    public void joinChannel(
        @PathVariable UUID channelId,
        @RequestBody AddChannelMemberRequest request
    ) {
        channelService.joinChannel(channelId, request.getUserId());
    }

    @PutMapping(path = "/channels/{channelId}")
    public void updateChannel(
        @PathVariable UUID channelId,
        @RequestBody Channel channel
    ) {
        channelService.updateChannel(channelId, channel);
    }

    @DeleteMapping(path = "/channels/{channelId}")
    public void deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
    }

    @DeleteMapping(path = "/channels/{channelId}/members/{userId}")
    public void leaveChannel(
        @PathVariable UUID channelId,
        @PathVariable UUID userId
    ) {
        channelService.leaveChannel(channelId, userId);
    }
}
