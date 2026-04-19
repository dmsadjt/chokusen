package com.dmsadjt.chokusen.dto;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddChannelMemberRequest {

    private UUID userId;
}
