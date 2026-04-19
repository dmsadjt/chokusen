package com.dmsadjt.chokusen.service.impl;

import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.Message;
import com.dmsadjt.chokusen.repository.ChannelRepository;
import com.dmsadjt.chokusen.repository.MessageRepository;
import com.dmsadjt.chokusen.service.MessageService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Override
    public Message createMessage(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public Message editMessage(UUID id, Message message) {
        Message existingMessage = messageRepository
            .findById(id)
            .orElseThrow(() -> new RuntimeException("Message not found"));
        existingMessage.setMessageContent(message.getMessageContent());
        existingMessage.setUpdatedAt(LocalDateTime.now());
        return messageRepository.save(existingMessage);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.deleteById(id);
    }

    @Override
    public List<Message> getMessagesInChannel(UUID channelId) {
        Channel channel = channelRepository
            .findById(channelId)
            .orElseThrow(() -> new RuntimeException("Channel not found"));
        return messageRepository.findByChannel(channel);
    }
}
