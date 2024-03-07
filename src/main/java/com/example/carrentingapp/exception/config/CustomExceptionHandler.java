package com.example.carrentingapp.exception.config;

import com.example.carrentingapp.exception.exception.BaseCustomException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(value = BaseCustomException.class)
    public ResponseEntity<ProblemDetail> handleBaseExceptions(BaseCustomException throwable){
        ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setType(URI.create(throwable.getClass().getSimpleName()));
        problemDetail.setTitle("An error occurred");
        problemDetail.setDetail(throwable.getMessage());
        return ResponseEntity.of(problemDetail).build();
    }
}
