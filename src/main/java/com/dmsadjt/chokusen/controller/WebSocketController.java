package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.MessageResponse;
import com.dmsadjt.chokusen.dto.SendMessageRequest;
import com.dmsadjt.chokusen.entity.Channel;
import com.dmsadjt.chokusen.entity.Message;
import com.dmsadjt.chokusen.entity.User;
import com.dmsadjt.chokusen.service.ChannelService;
import com.dmsadjt.chokusen.service.MessageService;
import com.dmsadjt.chokusen.service.UserService;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ChannelService channelService;

    @MessageMapping("/chat.send/{channelId}")
    public void sendMessage(
        @DestinationVariable UUID channelId,
        SendMessageRequest request
    ) {
        User user = userService.getUserById(request.getSenderId());
        Channel channel = channelService.getChannelById(channelId);

        Message message = new Message();
        message.setUser(user);
        message.setChannel(channel);
        message.setMessageContent(request.getContent());
        message.setCreatedAt(LocalDateTime.now());

        Message saved = messageService.createMessage(message);

        MessageResponse response = new MessageResponse(
            saved.getMessageId(),
            channelId,
            user.getUserId(),
            saved.getMessageContent(),
            saved.getCreatedAt()
        );

        messagingTemplate.convertAndSend(
            "/topic/channel." + channelId,
            response
        );
    }
}
