package com.example.carrentingapp.exception.exceptions.http_error_500;

public final class InvalidArgumentException extends BaseInternalErrorException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
