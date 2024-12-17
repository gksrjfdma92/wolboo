package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.entity.LectureRegistration;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LectureRegistrationRepository extends JpaRepository<LectureRegistration, Long> {
}
