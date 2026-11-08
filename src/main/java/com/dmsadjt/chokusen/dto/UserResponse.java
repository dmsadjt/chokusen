package com.dmsadjt.chokusen.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserResponse {

    private UUID userId;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
