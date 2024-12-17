package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.jaewon.wolboo.domain.Lecture.entity.QLecture.*;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Lecture> findLectureByName(String lectureName) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(lecture)
                        .where(
                                lecture.lectureName.eq(lectureName),
                                lecture.isDeleted.isFalse()
                        ).fetchFirst()
        );
    }

    @Override
    public Page<Lecture> findLectureListBySortingMethod(Pageable pageable, LectureSortingMethod sortingMethod, Boolean isOpenForRegistration) {
        QueryResults<Lecture> results = queryFactory
                .selectFrom(lecture)
                .where(
                        lecture.isDeleted.isFalse(),
                        isOpenForRegistration(isOpenForRegistration)
                )
                .orderBy(sortLectureByMethod(sortingMethod))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        return new PageImpl<Lecture>(results.getResults(), pageable, results.getTotal());

    }


    private BooleanExpression isOpenForRegistration(Boolean isOpenForRegistration) {
        if(isOpenForRegistration) {
            return lecture.lectureLimitNumber.gt(lecture.lectureRegistrationNumber);
        } else {
            return null;
        }
    }

    private NumberExpression<Double> registerRatio =
            lecture.lectureRegistrationNumber.doubleValue().divide(lecture.lectureLimitNumber.doubleValue());

    private OrderSpecifier sortLectureByMethod(LectureSortingMethod sortingMethod) {
        if(sortingMethod.equals(LectureSortingMethod.RECENT)) {
            return lecture.createdAt.desc();
        } else if(sortingMethod.equals(LectureSortingMethod.COUNT)) {
            return lecture.lectureRegistrationNumber.desc();
        } else if(sortingMethod.equals(LectureSortingMethod.RATE)) {
            return registerRatio.desc();
        }
        else {
            return null;
        }
    }


}
