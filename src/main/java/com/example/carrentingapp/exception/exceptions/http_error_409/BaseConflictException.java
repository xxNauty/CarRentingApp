package com.example.carrentingapp.exception.exceptions.http_error_409;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BaseConflictException extends RuntimeException {
    public BaseConflictException(String message) {
        super(message);
    }
}
