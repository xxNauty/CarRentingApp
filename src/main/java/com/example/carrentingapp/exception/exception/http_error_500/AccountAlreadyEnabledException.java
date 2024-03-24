package com.example.carrentingapp.exception.exception.http_error_500;

public final class AccountAlreadyEnabledException extends BaseInternalErrorException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
