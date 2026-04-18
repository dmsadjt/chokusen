package com.dmsadjt.chokusen.service;

import com.dmsadjt.chokusen.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(Message message);
    Message editMessage(UUID id, Message message);
    void deleteMessage(UUID id);
    List<Message> getMessagesInChannel(UUID channelId);
}
