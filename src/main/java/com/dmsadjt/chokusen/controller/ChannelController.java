package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.AddChannelMemberRequest;
import com.dmsadjt.chokusen.dto.UserResponse;
import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.service.ChannelService;
import com.dmsadjt.chokusen.service.UserService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    @Autowired
    private UserService userService;

    @GetMapping(path = "/channels")
    public ResponseEntity<List<Channel>> getAllChannels() {
        return ResponseEntity.ok(channelService.getAllChannels());
    }

    @GetMapping(path = "/channels/{channelId}")
    public ResponseEntity<Channel> getChannelById(
        @PathVariable UUID channelId
    ) {
        return ResponseEntity.ok(channelService.getChannelById(channelId));
    }

    @GetMapping(path = "/workspaces/{workspaceId}/channels")
    public ResponseEntity<List<Channel>> getChannelsInWorkspace(
        @PathVariable UUID workspaceId
    ) {
        return ResponseEntity.ok(
            channelService.getChannelsInWorkspace(workspaceId)
        );
    }

    @GetMapping(path = "/channels/{channelId}/members")
    public ResponseEntity<List<UserResponse>> getMembersInChannel(
        @PathVariable UUID channelId
    ) {
        return ResponseEntity.ok(
            channelService.getUsersInChannel(channelId)
                .stream()
                .map(ChannelController::toResponse)
                .collect(Collectors.toList())
        );
    }

    private static UserResponse toResponse(User user) {
        return new UserResponse(
            user.getUserId(),
            user.getUsername(),
            user.getEmail(),
            user.getCreatedAt()
        );
    }

    @PostMapping(path = "/workspaces/{workspaceId}/channels")
    public ResponseEntity<Channel> createChannelInWorkspace(
        @PathVariable UUID workspaceId,
        @RequestBody Channel channel,
        Authentication authentication
    ) {
        Channel createdChannel = channelService.createChannelInWorkspace(workspaceId, channel);
        User creator = userService.getUserByUsername(authentication.getName());
        channelService.joinChannel(createdChannel.getChannelId(), creator.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @PostMapping(path = "/channels/{channelId}/members")
    public ResponseEntity<Void> joinChannel(
        @PathVariable UUID channelId,
        @RequestBody AddChannelMemberRequest request
    ) {
        channelService.joinChannel(channelId, request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(path = "/channels/{channelId}")
    public ResponseEntity<Void> updateChannel(
        @PathVariable UUID channelId,
        @RequestBody Channel channel
    ) {
        channelService.updateChannel(channelId, channel);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/channels/{channelId}")
    public ResponseEntity<Void> deleteChannel(@PathVariable UUID channelId) {
        channelService.deleteChannel(channelId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/channels/{channelId}/members/{userId}")
    public ResponseEntity<Void> leaveChannel(
        @PathVariable UUID channelId,
        @PathVariable UUID userId
    ) {
        channelService.leaveChannel(channelId, userId);
        return ResponseEntity.noContent().build();
    }
}
