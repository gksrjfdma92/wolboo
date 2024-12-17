package com.jaewon.wolboo.domain.Lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateLectureRequest {
    private String lectureName;
    private Integer lectureLimitNumber;
    private BigDecimal lecturePrice;
}
