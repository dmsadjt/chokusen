package com.dmsadjt.chokusen.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MeResponse {

    private UUID userId;
    private String username;
    private String email;
}
