package com.example.carrentingapp.exception.exception.http_error_500;

public final class EmailAlreadyVerifiedException extends BaseInternalErrorException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
