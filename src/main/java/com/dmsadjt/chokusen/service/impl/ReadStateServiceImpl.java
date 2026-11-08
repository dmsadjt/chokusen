package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.ChannelReadState;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.entity.Workspace;
import com.dmsadjt.chokusen.exception.ResourceNotFoundException;
import com.dmsadjt.chokusen.repository.ChannelReadStateRepository;
import com.dmsadjt.chokusen.repository.ChannelRepository;
import com.dmsadjt.chokusen.repository.MessageRepository;
import com.dmsadjt.chokusen.repository.UserRepository;
import com.dmsadjt.chokusen.repository.WorkspaceRepository;
import com.dmsadjt.chokusen.service.ReadStateService;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReadStateServiceImpl implements ReadStateService {

    private final ChannelReadStateRepository readStateRepository;
    private final ChannelRepository channelRepository;
    private final WorkspaceRepository workspaceRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ChannelReadState markRead(UUID channelId, String username) {
        Channel channel = findChannel(channelId);
        User user = findUser(username);

        ChannelReadState state = readStateRepository
            .findByUserAndChannel(user, channel)
            .orElseGet(() -> {
                ChannelReadState created = new ChannelReadState();
                created.setUser(user);
                created.setChannel(channel);
                return created;
            });
        state.setLastReadAt(LocalDateTime.now());
        return readStateRepository.save(state);
    }

    @Override
    public List<ChannelReadState> getReadStates(UUID channelId) {
        return readStateRepository.findByChannel(findChannel(channelId));
    }

    @Override
    public Map<UUID, Long> getUnreadCounts(UUID workspaceId, String username) {
        Workspace workspace = workspaceRepository
            .findById(workspaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Workspace not found"));
        User user = findUser(username);

        Map<UUID, Long> counts = new LinkedHashMap<>();
        for (Channel channel : channelRepository.findChannelByWorkspace(workspace)) {
            long unread = readStateRepository
                .findByUserAndChannel(user, channel)
                .map(state ->
                    messageRepository.countByChannelAndUserNotAndCreatedAtAfter(
                        channel,
                        user,
                        state.getLastReadAt()
                    )
                )
                .orElseGet(() ->
                    messageRepository.countByChannelAndUserNot(channel, user)
                );
            counts.put(channel.getChannelId(), unread);
        }
        return counts;
    }

    private Channel findChannel(UUID channelId) {
        return channelRepository
            .findById(channelId)
            .orElseThrow(() -> new ResourceNotFoundException("Channel not found"));
    }

    private User findUser(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return user;
    }
}
