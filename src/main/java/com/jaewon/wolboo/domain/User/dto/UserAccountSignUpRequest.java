package com.jaewon.wolboo.domain.User.dto;

import com.jaewon.wolboo.domain.User.enums.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountSignUpRequest {

    @NotBlank(message = "이름은 입력은 필수입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9](?=\\S+$).{2,10}$")
    private String userName;

    @NotBlank(message = "비밀번호 입력은 필수입니다.")
    @Pattern(
            regexp = "^(?=(?:.*[a-z])(?:.*[A-Z])|(?:.*[a-z])(?:.*\\d)|(?:.*[A-Z])(?:.*\\d))[a-zA-Z\\d]{6,10}$",
            message = "비밀번호는 영어 대문자, 영어 소문자, 숫자 중 2가지 이상을 조합하여 6~10자리 이내로 입력해주세요.")
    private String password;

    @NotBlank(message = "이메일은 입력은 필수입니다.")
    @Email(message = "유효하지 않은 이메일 형식입니다.",
            regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    private String email;

    @Pattern(message = "전화번호 형식은 010-0000-0000로 입력해주세요.",
            regexp = "^010-\\d{4}-\\d{4}$")
    private String phoneNumber;
    @NotNull(message = "유저 구분 입력은 필수입니다.")
    private UserRole userRole;
}
