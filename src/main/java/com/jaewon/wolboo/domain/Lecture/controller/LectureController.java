package com.jaewon.wolboo.domain.Lecture.controller;

import com.jaewon.wolboo.domain.Lecture.dto.CreateLectureRequest;
import com.jaewon.wolboo.domain.Lecture.dto.LectureRegistrationResponse;
import com.jaewon.wolboo.domain.Lecture.dto.LectureResponse;
import com.jaewon.wolboo.domain.Lecture.enums.LectureSortingMethod;
import com.jaewon.wolboo.domain.Lecture.service.LectureService;
import com.jaewon.wolboo.domain.User.dto.AuthUserDto;
import com.jaewon.wolboo.security.jwt.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/lecture")
public class LectureController {

    private final LectureService lectureService;
    private final JwtProvider jwtProvider;

    @PostMapping("/create")
    public ResponseEntity<String> createLecture(
            @Valid @RequestBody CreateLectureRequest request, HttpServletRequest servletRequest) throws Exception
    {
        String accessToken = jwtProvider.extractAccessToken(servletRequest);
        AuthUserDto authUserDto = jwtProvider.validateToken(accessToken);
        lectureService.createLecture(request, authUserDto.getEmail());
        return ResponseEntity.ok("lecture register success");
    }
//
    @GetMapping("/list/page")
    public ResponseEntity<Page<LectureResponse>> getLecturePage(
            @Valid Pageable pageable, @Valid @RequestParam LectureSortingMethod lectureSortingMethod,
            @RequestParam Boolean isOpenForRegistration,
            HttpServletRequest servletRequest)
    throws Exception
    {
        String accessToken = jwtProvider.extractAccessToken(servletRequest);
        AuthUserDto authUserDto = jwtProvider.validateToken(accessToken);
        Page<LectureResponse> lectureResponses = lectureService.searchLectures(lectureSortingMethod, pageable, isOpenForRegistration);
        return ResponseEntity.ok(lectureResponses);
    }

    @PostMapping("/apply")
    public ResponseEntity<List<LectureRegistrationResponse>> applyToLectureList
            (@Valid @RequestBody List<Long> requests, HttpServletRequest servletRequest) throws Exception
    {
        String accessToken = jwtProvider.extractAccessToken(servletRequest);
        AuthUserDto authUserDto = jwtProvider.validateToken(accessToken);
        List<LectureRegistrationResponse> registrationResponseList = lectureService.applyToLectureList(requests, authUserDto.getEmail());
        return ResponseEntity.ok(registrationResponseList);
    }

    @GetMapping("/myList/registered")
    public ResponseEntity<Page<LectureResponse>> viewRegisteredLectures(
            @Valid Pageable pageable,  HttpServletRequest servletRequest)
            throws Exception
    {
        String accessToken = jwtProvider.extractAccessToken(servletRequest);
        AuthUserDto authUserDto = jwtProvider.validateToken(accessToken);
        Page<LectureResponse> lectureResponses = lectureService.viewRegisteredLectures(pageable, authUserDto.getEmail());
        return ResponseEntity.ok(lectureResponses);
    }
}
