package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture, Long>, LectureRepositoryCustom {

    Optional<Lecture> findByIdAndIsDeleted(Long lectureId, Boolean isDeleted);
}
