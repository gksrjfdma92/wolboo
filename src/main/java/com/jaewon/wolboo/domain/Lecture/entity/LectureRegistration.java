package com.jaewon.wolboo.domain.Lecture.entity;

import com.jaewon.wolboo.domain.User.entity.UserAccount;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class LectureRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserAccount userAccount;

    private Boolean isDeleted = false;

    public LectureRegistration(Lecture lecture, UserAccount userAccount) {
        this.lecture = lecture;
        this.userAccount = userAccount;
        this.isDeleted = false;
    }


}
