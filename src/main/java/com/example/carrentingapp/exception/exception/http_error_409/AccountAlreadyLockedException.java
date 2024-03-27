package com.example.carrentingapp.exception.exception.http_error_409;

public final class AccountAlreadyLockedException extends BaseConflictException {
    public AccountAlreadyLockedException(String message) {
        super(message);
    }
}
