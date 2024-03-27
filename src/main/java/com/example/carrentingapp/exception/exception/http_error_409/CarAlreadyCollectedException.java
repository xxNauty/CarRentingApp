package com.example.carrentingapp.exception.exception.http_error_409;

public final class CarAlreadyCollectedException extends BaseConflictException {
    public CarAlreadyCollectedException(String message) {
        super(message);
    }
}
