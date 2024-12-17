package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.entity.LectureRegistration;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRegistrationRepository extends JpaRepository<LectureRegistration, Long> {

    Optional<LectureRegistration> findByUserAccountAndLectureAndIsDeleted(UserAccount userAccount, Lecture lecture, Boolean isDeleted);
}
