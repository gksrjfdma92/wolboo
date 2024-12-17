package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.dto.LectureResponse;
import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface LectureRepositoryCustom {

    Optional<Lecture> findLectureByName(String lectureName);

    Page<LectureResponse> findLectureListBySortingMethod(Pageable pageable, LectureSortingMethod sortingMethod, Boolean isOpenForRegistration);

    Page<LectureResponse> findMyLectureList(Pageable pageable, UserAccount userAccount);
}
