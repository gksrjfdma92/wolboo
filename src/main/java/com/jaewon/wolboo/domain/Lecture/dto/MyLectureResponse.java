package com.jaewon.wolboo.domain.Lecture.dto;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MyLectureResponse {

    private Long lectureId;
    private String lectureName;
    private String teacherName;
    private Integer lectureLimitNumber;
    private Integer lectureRegisterNumber;
    private BigDecimal lecturePrice;

    @QueryProjection
    public MyLectureResponse(Lecture lecture, UserAccount userAccount) {
        this.lectureId = lecture.getId();
        this.lectureName = lecture.getLectureName();
        this.teacherName = userAccount.getUserName();
        this.lectureLimitNumber = lecture.getLectureLimitNumber();
        this.lectureRegisterNumber = lecture.getLectureRegistrationNumber();
        this.lecturePrice = lecture.getLecturePrice();
    }
}
