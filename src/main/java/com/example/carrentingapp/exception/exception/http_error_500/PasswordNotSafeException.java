package com.example.carrentingapp.exception.exception.http_error_500;

public class PasswordNotSafeException extends BaseInternalErrorException {
    public PasswordNotSafeException(String message) {
        super(message);
    }
}
