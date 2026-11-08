package com.dmsadjt.chokusen.dto;

import java.util.UUID;

public record UnreadCountResponse(UUID channelId, long unreadCount) {}
