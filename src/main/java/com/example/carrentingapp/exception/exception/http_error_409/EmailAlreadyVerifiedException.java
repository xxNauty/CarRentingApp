package com.example.carrentingapp.exception.exception.http_error_409;

public final class EmailAlreadyVerifiedException extends BaseConflictException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
