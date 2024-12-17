package com.jaewon.wolboo.security.jwt;

import com.jaewon.wolboo.domain.User.dto.AuthUserDto;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.domain.User.repository.UserRepository;
import com.jaewon.wolboo.security.jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {


    public static final long ACCESS_TOKEN_EXPIRE_SECOND = 24 * 60 * 60;
    public static final long REFRESH_TOKEN_EXPIRE_SECOND = 12 * 30 * 24 * 60 * 60;

    private final UserRepository userRepository;

    private byte[] secretKey;

    @PostConstruct
    private void init() {
        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded();
    }

    public TokenDto generateToken(String email) {
        Date now = new Date();

        Claims claims = Jwts.claims();
        claims.put("email", email);

        String accessToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(now.getTime() + Duration.ofSeconds(ACCESS_TOKEN_EXPIRE_SECOND).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now.getTime() + Duration.ofSeconds(REFRESH_TOKEN_EXPIRE_SECOND).toMillis()))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

        return TokenDto.builder()
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
    }

    public String getEmail(String token) {
        return getClaims(token).get("email", String.class);
    }

    public boolean isTokenExpired(String token) {
        return Objects.requireNonNull(getClaims(token)).getExpiration().before(new Date());
    }

    public String extractAccessToken(HttpServletRequest request) {

        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }

        return authorizationHeader.split("Bearer ")[1];

    }

    public AuthUserDto validateToken(String token) {
        if(isTokenExpired(token)) {
            return null;
        }
        String email = getEmail(token);
        Optional<UserAccount> optionalUserAccount = userRepository.findActiveUserByEmail(email);
        return optionalUserAccount.map(userAccount -> AuthUserDto.builder()
                .userName(userAccount.getUserName())
                .email(userAccount.getEmailAddress())
                .build()).orElse(null);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            return null;
        } catch (Exception e) {
            return null;
        }
    }


}
