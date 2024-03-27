package com.example.carrentingapp.exception.exception.http_error_409;

public final class CarNotLockedException extends BaseConflictException {
    public CarNotLockedException(String message) {
        super(message);
    }
}
