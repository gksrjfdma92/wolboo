package com.jaewon.wolboo.domain.User.repository;

import com.jaewon.wolboo.domain.User.entity.QUserAccount;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Repository;
import java.util.Optional;

import static com.jaewon.wolboo.domain.User.entity.QUserAccount.*;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserAccount> findActiveUserByEmail(String email) {
        return Optional.ofNullable(queryFactory
                .selectFrom(userAccount)
                .where(userAccount.emailAddress.eq(email),
                        userAccount.isDeleted.isFalse())
                .fetchFirst());
    }
}
