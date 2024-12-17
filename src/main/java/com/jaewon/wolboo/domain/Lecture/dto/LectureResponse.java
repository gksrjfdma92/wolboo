package com.jaewon.wolboo.domain.Lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LectureResponse {

    private Long lectureId;
    private String lectureName;
    private Integer lectureLimitNumber;
    private Integer lectureRegisterNumber;
    private BigDecimal lecturePrice;

}
