package com.example.carrentingapp.exception.exception.http_error_404;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class BaseNotFoundException extends RuntimeException{
    public BaseNotFoundException(String message) {
        super(message);
    }
}
