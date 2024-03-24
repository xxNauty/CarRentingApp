package com.example.carrentingapp.exception.exception.http_error_500;

public final class UserNotLockedException extends BaseInternalErrorException {
    public UserNotLockedException(String message) {
        super(message);
    }
}
