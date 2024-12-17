package com.jaewon.wolboo.domain.User.Service;

import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.domain.User.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccount userAccount = userRepository.findActiveUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return User.builder()
                .username(userAccount.getEmailAddress())
                .password(userAccount.getPassword())
                .roles(userAccount.getUserRole())
                .build();
    }
}
