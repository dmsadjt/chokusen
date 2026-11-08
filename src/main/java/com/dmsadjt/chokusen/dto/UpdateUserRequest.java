package com.dmsadjt.chokusen.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserRequest {

    @Email
    @Size(min = 1)
    private String email;

    @Size(min = 8, max = 72)
    private String password;
}
