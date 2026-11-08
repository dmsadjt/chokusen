package com.dmsadjt.chokusen.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record ReadReceiptResponse(UUID userId, String username, LocalDateTime readAt) {}
