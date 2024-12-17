package com.jaewon.wolboo.domain.Lecture.service;

import com.jaewon.wolboo.domain.Lecture.dto.CreateLectureRequest;
import com.jaewon.wolboo.domain.Lecture.dto.LectureRegistrationResponse;
import com.jaewon.wolboo.domain.Lecture.dto.LectureResponse;
import com.jaewon.wolboo.domain.Lecture.entity.Lecture;
import com.jaewon.wolboo.domain.Lecture.entity.LectureRegistration;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import com.jaewon.wolboo.domain.Lecture.repository.LectureRepository;
import com.jaewon.wolboo.domain.User.entity.UserAccount;
import com.jaewon.wolboo.domain.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureService {

    private final LectureRepository lectureRepository;
    private final UserRepository userRepository;
    private final LectureRegisterService lectureRegisterService;

    public void createLecture(CreateLectureRequest registerRequest, String email) throws Exception {
        UserAccount userAccount = userRepository.findActiveUserByEmail(email).orElseThrow(() -> new Exception("User not found"));
        if(!userAccount.getUserRole().equals("TEACHER")) {
            throw new Exception("User is not TEACHER");
        }
        Optional<Lecture> lectureByName = lectureRepository.findLectureByName(registerRequest.getLectureName());
        if (lectureByName.isPresent()) {
            throw new Exception("Lecture already registered");
        }
        Lecture lecture = new Lecture(
                registerRequest.getLectureName(),
                registerRequest.getLectureLimitNumber(),
                registerRequest.getLecturePrice(),
                userAccount
        );

        lectureRepository.save(lecture);
    }

    public Page<LectureResponse> searchLectures(LectureSortingMethod lectureSortingMethod, Pageable pageable, Boolean isOpenForRegistration) throws Exception
    {
        Page<LectureResponse> lectureResponses = lectureRepository.findLectureListBySortingMethod(pageable, lectureSortingMethod, isOpenForRegistration)
                .map(it -> LectureResponse.builder()
                        .lectureId(it.getId())
                        .lectureName(it.getLectureName())
                        .lectureLimitNumber(it.getLectureLimitNumber())
                        .lecturePrice(it.getLecturePrice())
                        .lectureRegisterNumber(it.getLectureRegistrationNumber())
                        .teacherName(it.getUserAccount().getUserName())
                        .build()
                );

        return lectureResponses;
    }

    public List<LectureRegistrationResponse> applyToLectureList(List<Long> lectureIdList, String email) throws Exception {
        UserAccount userAccount = userRepository.findActiveUserByEmail(email).orElseThrow(() -> new Exception("User not found"));
        if(!userAccount.getUserRole().equals("STUDENT")) {
            throw new Exception("User is not STUDENT");
        }
        List<LectureRegistrationResponse> registrationResponseList = new ArrayList<>();
        for(Long lectureId : lectureIdList) {
            boolean registered = false;
            try {
                registered = lectureRegisterService.registerOneLecture(lectureId, userAccount);
            } catch (Exception e) {
                System.out.println("Failed to register for lecture " + lectureId + ": " + e.getMessage());
            }

            LectureRegistrationResponse registerResponse = LectureRegistrationResponse.builder()
                    .registerSuccess(registered)
                    .lectureId(lectureId)
                    .build();

            registrationResponseList.add(registerResponse);
        }
        return registrationResponseList;
    }




}
