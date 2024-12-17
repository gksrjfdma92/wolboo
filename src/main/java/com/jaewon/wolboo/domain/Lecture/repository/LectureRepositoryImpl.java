package com.jaewon.wolboo.domain.Lecture.repository;

import com.jaewon.wolboo.domain.Lecture.dto.LectureResponse;
import com.jaewon.wolboo.domain.Lecture.dto.QLectureResponse;
import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
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

import java.util.List;
import java.util.Optional;

import static com.jaewon.wolboo.domain.Lecture.entity.QLecture.*;
import static com.jaewon.wolboo.domain.Lecture.entity.QLectureRegistration.*;
import static com.jaewon.wolboo.domain.User.entity.QUserAccount.*;

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
    public Page<LectureResponse> findLectureListBySortingMethod(Pageable pageable, LectureSortingMethod sortingMethod, Boolean isOpenForRegistration) {
        List<LectureResponse> results = queryFactory
                .select(new QLectureResponse(lecture, lecture.userAccount))
                .from(lecture)
                .join(lecture.userAccount, userAccount)
                .where(
                        lecture.isDeleted.isFalse(),
                        isOpenForRegistration(isOpenForRegistration)
                )
                .orderBy(sortLectureByMethod(sortingMethod))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(lecture.count().coalesce(0L))
                .from(lecture)
                .join(lecture.userAccount, userAccount)
                .where(
                        lecture.isDeleted.isFalse(),
                        isOpenForRegistration(isOpenForRegistration)
                )
                .fetchOne();

        return new PageImpl<LectureResponse>(results, pageable, total);

    }

    @Override
    public Page<LectureResponse> findMyLectureList(Pageable pageable, UserAccount userAccount) {
        List<LectureResponse> results = queryFactory
                .select(new QLectureResponse(lecture, lecture.userAccount))
                .from(lecture)
                .join(lectureRegistration).on(lectureRegistration.lecture.eq(lecture))
                .where(
                        lectureRegistration.userAccount.eq(userAccount),
                        lecture.isDeleted.isFalse()
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(lecture.count().coalesce(0L))
                .from(lecture)
                .join(lectureRegistration).on(lectureRegistration.lecture.eq(lecture))
                .where(
                        lectureRegistration.userAccount.eq(userAccount),
                        lecture.isDeleted.isFalse()
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total);

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
