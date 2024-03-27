package com.example.carrentingapp.exception.exception.http_error_409;

public final class AccountAlreadyEnabledException extends BaseConflictException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
