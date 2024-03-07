package com.example.carrentingapp.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseCustomException extends RuntimeException{
    public BaseCustomException(String message) {
        super(message);
    }
}
