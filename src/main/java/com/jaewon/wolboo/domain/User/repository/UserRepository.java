package com.jaewon.wolboo.domain.User.repository;

import com.jaewon.wolboo.domain.User.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserAccount, Long>, UserRepositoryCustom {

    List<UserAccount> findUserAccountsByIsDeleted(Boolean isDeleted);
}
