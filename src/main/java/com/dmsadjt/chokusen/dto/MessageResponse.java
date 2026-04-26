package com.dmsadjt.chokusen.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse {

    private UUID messageId;
    private UUID channelId;
    private UUID senderId;
    private String content;
    private LocalDateTime sentAt;
}
