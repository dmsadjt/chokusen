package com.dmsadjt.chokusen.controller;

import com.dmsadjt.chokusen.dto.ReadReceiptResponse;
import com.dmsadjt.chokusen.dto.UnreadCountResponse;
import com.dmsadjt.chokusen.entity.ChannelReadState;
import com.dmsadjt.chokusen.service.ReadStateService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ReadStateController {

    private final ReadStateService readStateService;
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping(path = "/workspaces/{workspaceId}/unread")
    public ResponseEntity<List<UnreadCountResponse>> getUnreadCounts(
        @PathVariable UUID workspaceId,
        Authentication authentication
    ) {
        return ResponseEntity.ok(
            readStateService
                .getUnreadCounts(workspaceId, authentication.getName())
                .entrySet()
                .stream()
                .map(e -> new UnreadCountResponse(e.getKey(), e.getValue()))
                .toList()
        );
    }

    @GetMapping(path = "/channels/{channelId}/reads")
    public ResponseEntity<List<ReadReceiptResponse>> getReads(
        @PathVariable UUID channelId
    ) {
        return ResponseEntity.ok(
            readStateService
                .getReadStates(channelId)
                .stream()
                .map(ReadStateController::toReceipt)
                .toList()
        );
    }

    @PostMapping(path = "/channels/{channelId}/read")
    public ResponseEntity<Void> markRead(
        @PathVariable UUID channelId,
        Authentication authentication
    ) {
        ChannelReadState state = readStateService.markRead(
            channelId,
            authentication.getName()
        );
        messagingTemplate.convertAndSend(
            "/topic/channel." + channelId + ".reads",
            toReceipt(state)
        );
        return ResponseEntity.noContent().build();
    }

    private static ReadReceiptResponse toReceipt(ChannelReadState state) {
        return new ReadReceiptResponse(
            state.getUser().getUserId(),
            state.getUser().getUsername(),
            state.getLastReadAt()
        );
    }
}
