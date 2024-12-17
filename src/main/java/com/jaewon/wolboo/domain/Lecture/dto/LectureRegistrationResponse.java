package com.jaewon.wolboo.domain.Lecture.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LectureRegistrationResponse {

    private Long lectureId;
    private Boolean registerSuccess;

}
