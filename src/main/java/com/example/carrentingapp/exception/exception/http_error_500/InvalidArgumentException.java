package com.example.carrentingapp.exception.exception.http_error_500;

public class InvalidArgumentException extends BaseInternalErrorException{
    public InvalidArgumentException(String message) {
        super(message);
    }
}
