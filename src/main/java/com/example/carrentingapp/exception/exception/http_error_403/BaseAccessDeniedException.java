package com.example.carrentingapp.exception.exception.http_error_403;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BaseAccessDeniedException extends RuntimeException {
    public BaseAccessDeniedException(String message) {
        super(message);
    }
}
