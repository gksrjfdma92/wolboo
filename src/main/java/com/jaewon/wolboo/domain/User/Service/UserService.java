package com.jaewon.wolboo.domain.User.Service;

import com.jaewon.wolboo.domain.User.dto.UserAccountLoginRequest;
import com.jaewon.wolboo.domain.User.dto.UserAccountResponse;
import com.jaewon.wolboo.domain.User.dto.UserAccountSignUpRequest;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.domain.User.repository.UserRepository;
import com.jaewon.wolboo.security.jwt.JwtProvider;
import com.jaewon.wolboo.security.jwt.dto.TokenDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class UserService{

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;


    // 회원가입
    @Transactional
    public String signupUser(@Valid UserAccountSignUpRequest signUpRequest) {
        String encodePassword = passwordEncoder.encode(signUpRequest.getPassword());
        Optional<UserAccount> prevActiveUserAccount = userRepository.findActiveUserByEmail(signUpRequest.getEmail());
        if (prevActiveUserAccount.isPresent()) {
            throw new IllegalStateException("User account already exists");
        }
        UserAccount userAccount = new UserAccount(
                signUpRequest.getUserName(), signUpRequest.getEmail(), signUpRequest.getPhoneNumber(),
                encodePassword, signUpRequest.getUserRole().toString()
        );
        userRepository.save(userAccount);
        return "User account created";
    }

    // 모든 유저 리스트
    public List<UserAccountResponse> getActiveUsers() {
        return userRepository.findUserAccountsByIsDeleted(false)
                .stream()
                .map(it -> new UserAccountResponse(it))
                .collect(Collectors.toList());
    }

    // 로그인
    public TokenDto loginUser(@Valid UserAccountLoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        return jwtProvider.generateToken(loginRequest.getEmail());
    }


    // Token 만들기
    public UsernamePasswordAuthenticationToken getAuthenticationToken(String email, String password) {
        return new UsernamePasswordAuthenticationToken(email, password);
    }

}
