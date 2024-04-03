package com.example.carrentingapp.exception.exceptions.http_error_409;

public final class NotLockedException extends BaseConflictException {
    public NotLockedException(String message) {
        super(message);
    }
}
