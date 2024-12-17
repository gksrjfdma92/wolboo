package com.jaewon.wolboo.domain.User.Service;

import com.jaewon.wolboo.domain.User.Service.UserService;
import com.jaewon.wolboo.domain.User.dto.UserAccountLoginRequest;
import com.jaewon.wolboo.domain.User.dto.UserAccountSignUpRequest;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.domain.User.repository.UserRepository;
import com.jaewon.wolboo.security.jwt.JwtProvider;
import com.jaewon.wolboo.security.jwt.dto.TokenDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.jaewon.wolboo.domain.User.enums.UserRole;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class UserServiceTest {

    @InjectMocks
    private UserService userAccountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignupUser_Success() {
        // Given
        UserAccountSignUpRequest request = new UserAccountSignUpRequest(
                "testUser", "Password123", "test@example.com", "010-1234-5678", UserRole.STUDENT);

        when(userRepository.findActiveUserByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserAccount.class))).thenReturn(new UserAccount());

        // When
        String result = userAccountService.signupUser(request);

        // Then
        assertEquals("User account created", result);
        verify(passwordEncoder, times(1)).encode("Password123");
        verify(userRepository, times(1)).save(any(UserAccount.class));
    }

    @Test
    void testSignupUser_ThrowsException_WhenUserAlreadyExists() {
        // Given
        UserAccountSignUpRequest request = new UserAccountSignUpRequest(
                "testUser", "Password123", "test@example.com", "010-1234-5678", UserRole.STUDENT);

        when(userRepository.findActiveUserByEmail(anyString())).thenReturn(Optional.of(new UserAccount()));

        // When & Then
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            userAccountService.signupUser(request);
        });

        assertEquals("User account already exists", exception.getMessage());
        verify(userRepository, never()).save(any(UserAccount.class));
    }

    @Test
    void testLoginUser_Success() {
        // Given
        UserAccountLoginRequest request = new UserAccountLoginRequest("test@example.com", "Password123");
        Authentication mockAuth = mock(Authentication.class);

        TokenDto mockTokenDto = TokenDto.builder()
                .accessToken("dummyAccessToken")
                .refreshToken("dummyRefreshToken")
                .build();

        doReturn(mockAuth)
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        doReturn(mockTokenDto)
                .when(jwtProvider)
                .generateToken(anyString());

        // When
        TokenDto result = userAccountService.loginUser(request);

        // Then
        assertNotNull(result);
        assertEquals("dummyAccessToken", result.getAccessToken());
        assertEquals("dummyRefreshToken", result.getRefreshToken());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtProvider, times(1)).generateToken(anyString());
    }

    @Test
    void testLoginUser_Fails_WhenAuthenticationFails() {
        // Given
        UserAccountLoginRequest request = new UserAccountLoginRequest("test@example.com", "wrongPassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Authentication failed"));

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userAccountService.loginUser(request);
        });

        assertEquals("Authentication failed", exception.getMessage());
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }
}
