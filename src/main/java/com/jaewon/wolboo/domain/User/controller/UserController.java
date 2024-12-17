package com.jaewon.wolboo.domain.User.controller;

import com.jaewon.wolboo.domain.User.Service.UserService;
import com.jaewon.wolboo.domain.User.dto.UserAccountLoginRequest;
import com.jaewon.wolboo.domain.User.dto.UserAccountResponse;
import com.jaewon.wolboo.domain.User.dto.UserAccountSignUpRequest;
import com.jaewon.wolboo.security.jwt.JwtProvider;
import com.jaewon.wolboo.security.jwt.dto.TokenDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody UserAccountSignUpRequest userAccountSignUpRequest) {
        String response = userService.signupUser(userAccountSignUpRequest);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserAccountResponse>> getAllUsers() {
        List<UserAccountResponse> userAccountResponseList = userService.getActiveUsers();
        return ResponseEntity.ok(userAccountResponseList);
    }

    @GetMapping("/login")
    public ResponseEntity<TokenDto> login(
            @RequestParam String email,
            @RequestParam String password
    ) {
        TokenDto tokenDto = userService.loginUser(new UserAccountLoginRequest(email, password));
        return ResponseEntity.ok(tokenDto);
    }
}
