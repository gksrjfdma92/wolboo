package com.jaewon.wolboo.domain.User.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class AuthUserDto {
    private String email;
    private String userName;

}
