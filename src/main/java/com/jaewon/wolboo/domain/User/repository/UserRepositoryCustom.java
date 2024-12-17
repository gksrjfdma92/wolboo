package com.jaewon.wolboo.domain.User.repository;

import com.jaewon.wolboo.domain.User.entity.UserAccount;

import java.util.Optional;

public interface UserRepositoryCustom {

    Optional<UserAccount> findActiveUserByEmail(String email);
}
