package com.example.carrentingapp.exception.exception.http_error_500;

public final class AccountAlreadyLockedException extends BaseInternalErrorException {
    public AccountAlreadyLockedException(String message) {
        super(message);
    }
}
