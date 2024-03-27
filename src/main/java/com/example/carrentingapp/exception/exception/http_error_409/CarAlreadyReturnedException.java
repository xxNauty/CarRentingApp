package com.example.carrentingapp.exception.exception.http_error_409;

public final class CarAlreadyReturnedException extends BaseConflictException {
    public CarAlreadyReturnedException(String message) {
        super(message);
    }
}
