package com.jaewon.wolboo.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserAccountLoginRequest {
    private String email;
    private String password;
}
