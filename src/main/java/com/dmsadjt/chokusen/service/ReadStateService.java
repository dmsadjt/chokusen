package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.ChannelReadState;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReadStateService {
    ChannelReadState markRead(UUID channelId, String username);

    List<ChannelReadState> getReadStates(UUID channelId);

    Map<UUID, Long> getUnreadCounts(UUID workspaceId, String username);
}
