package com.example.carrentingapp.exception.config;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;
import com.example.carrentingapp.exception.exception.http_error_500.BaseInternalErrorException;
import com.example.carrentingapp.exception.exception.http_error_404.BaseNotFoundException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.sql.SQLException;

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(value = BaseInternalErrorException.class) //500
    public ResponseEntity<ProblemDetail> handleInternalServerErrorExceptions(BaseInternalErrorException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("An error occurred");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(value = BaseNotFoundException.class) //404
    public ResponseEntity<ProblemDetail> handleNotFoundExceptions(BaseNotFoundException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("Value not found");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(value = BaseAccessDeniedException.class) //403
    public ResponseEntity<ProblemDetail> handleAccessDeniedExceptions(BaseAccessDeniedException exception){
        ProblemDetail problemDetail = ProblemDetail.forStatus(403);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("Access denied");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }
}
