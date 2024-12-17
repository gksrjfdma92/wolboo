package com.jaewon.wolboo.global;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            errors.put(error.getDefaultMessage(), error.getDefaultMessage());
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // 일반 예외 처리
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleGeneralException(Exception e) {
        System.err.println("Exception caught: " + e.getMessage());

        return new ResponseEntity<>("서버에서 오류가 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 잘못된 요청 처리
    @ExceptionHandler(IllegalArgumentException.class)
    protected ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("잘못된 요청입니다: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // NullPointerException 처리
    @ExceptionHandler(NullPointerException.class)
    protected ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>("서버에서 NullPointerException이 발생했습니다.",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
