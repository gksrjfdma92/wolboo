package com.jaewon.wolboo.domain.Lecture.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class CreateLectureRequest {
    @NotNull(message = "강의 이름은 필수 입력값입니다.")
    private String lectureName;
    @NotNull(message = "강의 제한 인원은 필수 입력값입니다.")
    @Positive(message = "강의 제한 인원은 0보다 커야 합니다.")
    private Integer lectureLimitNumber;
    @NotNull(message = "강의료는 필수 입력값입니다.")
    @Positive(message = "강의료는 0보다 커야 합니다.")
    private BigDecimal lecturePrice;
}
