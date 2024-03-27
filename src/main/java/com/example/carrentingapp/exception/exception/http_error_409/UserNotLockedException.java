package com.example.carrentingapp.exception.exception.http_error_409;

public final class UserNotLockedException extends BaseConflictException {
    public UserNotLockedException(String message) {
        super(message);
    }
}
