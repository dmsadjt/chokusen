package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.entity.Message;
import com.dmsadjt.chokusen.service.MessageService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/v1")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping(path = "/channels/{channelId}/messages")
    public ResponseEntity<List<Message>> getAllMessages(
        @PathVariable UUID channelId
    ) {
        return ResponseEntity.ok(
            messageService.getMessagesInChannel(channelId)
        );
    }

    @PutMapping(path = "/channels/{channelId}/messages/{messageId}")
    public ResponseEntity<Void> editMessage(
        @PathVariable UUID channelId,
        @PathVariable UUID messageId,
        @RequestBody Message message
    ) {
        messageService.editMessage(messageId, message);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/channels/{channelId}/messages/{messageId}")
    public ResponseEntity<Void> deleteMessage(
        @PathVariable UUID channelId,
        @PathVariable UUID messageId
    ) {
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
