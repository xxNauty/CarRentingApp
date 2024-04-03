package com.example.carrentingapp.exception.exceptions.http_error_500;

public class InvalidInternalArgumentException extends BaseInternalErrorException {
    public InvalidInternalArgumentException(String message) {
        super(message);
    }
}
