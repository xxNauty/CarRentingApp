package com.example.carrentingapp.exception;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;
import com.example.carrentingapp.exception.exception.http_error_404.BaseNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_409.BaseConflictException;
import com.example.carrentingapp.exception.exception.http_error_500.BaseInternalErrorException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

@RestControllerAdvice
class CustomExceptionHandler {

    @ExceptionHandler(value = BaseInternalErrorException.class) //500
    public ResponseEntity<ProblemDetail> handleInternalServerErrorExceptions(BaseInternalErrorException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(500);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("An error occurred");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(value = BaseConflictException.class) //409
    public ResponseEntity<ProblemDetail> handleInternalServerErrorExceptions(BaseConflictException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(409);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("Conflicted");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(value = BaseNotFoundException.class) //404
    public ResponseEntity<ProblemDetail> handleNotFoundExceptions(BaseNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(404);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("Value not found");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }

    @ExceptionHandler(value = BaseAccessDeniedException.class) //403
    public ResponseEntity<ProblemDetail> handleAccessDeniedExceptions(BaseAccessDeniedException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(403);
        problemDetail.setType(URI.create(exception.getClass().getSimpleName()));
        problemDetail.setTitle("Access denied");
        problemDetail.setDetail(exception.getMessage());

        return ResponseEntity.of(problemDetail).build();
    }
}
