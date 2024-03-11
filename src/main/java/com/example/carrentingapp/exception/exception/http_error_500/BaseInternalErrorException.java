package com.example.carrentingapp.exception.exception.http_error_500;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class BaseInternalErrorException extends RuntimeException{
    public BaseInternalErrorException(String message) {
        super(message);
    }
}
