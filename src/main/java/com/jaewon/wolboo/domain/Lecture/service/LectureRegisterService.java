package com.jaewon.wolboo.domain.Lecture.service;

import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.entity.LectureRegistration;
import com.jaewon.wolboo.domain.Lecture.repository.LectureRegistrationRepository;
import com.jaewon.wolboo.domain.Lecture.repository.LectureRepository;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.global.RedisLockService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LectureRegisterService {

    private final LectureRepository lectureRepository;
    private final LectureRegistrationRepository lectureRegistrationRepository;
    private final RedisLockService redisLockService;

    private static final int MAX_RETRY_COUNT = 5;
    private static final int RETRY_DELAY_MS = 200;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Boolean registerOneLecture(Long lectureId, UserAccount userAccount) throws Exception {
        String lockKey = "lecture:lock:" + lectureId;
        String lockValue = UUID.randomUUID().toString();

        boolean lockAcquired = false;

        for (int i = 0; i < MAX_RETRY_COUNT; i++) {
            lockAcquired = redisLockService.acquireLock(lockKey, lockValue, 10); // 10초 만료 시간
            if (lockAcquired) {
                break;
            }
            Thread.sleep(RETRY_DELAY_MS);
        }

        // 1. Redis 락 획득
        if (!lockAcquired) {
            throw new Exception("Too many requests. Please try again later.");
        }

        try {
            // 2. 비즈니스 로직 수행
            Lecture lecture = lectureRepository.findByIdAndIsDeleted(lectureId, false)
                    .orElseThrow(() -> new Exception("Lecture not found"));

            Optional<LectureRegistration> byUserAccountAndLecture = lectureRegistrationRepository.findByUserAccountAndLectureAndIsDeleted(userAccount, lecture,false);

            if(byUserAccountAndLecture.isPresent()) {
                throw new IllegalArgumentException("Lecture already registered");
            }

            if (lecture.getLectureLimitNumber() <= lecture.getLectureRegistrationNumber()) {
                return false;
            } else {
                lecture.changeLectureRegistrationNumber(lecture.getLectureRegistrationNumber() + 1);
                lectureRepository.save(lecture);

                LectureRegistration lectureRegistration = new LectureRegistration(lecture, userAccount);
                lectureRegistrationRepository.save(lectureRegistration);

                return true;
            }
        } finally {
            // 3. 락 해제
            redisLockService.releaseLock(lockKey, lockValue);
        }
    }

}
