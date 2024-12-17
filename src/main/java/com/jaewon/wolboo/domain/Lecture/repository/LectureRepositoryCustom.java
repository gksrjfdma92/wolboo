package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LectureRepositoryCustom {

    Optional<Lecture> findLectureByName(String lectureName);

    Page<Lecture> findLectureListBySortingMethod(Pageable pageable, LectureSortingMethod sortingMethod, Boolean isOpenForRegistration);

}
