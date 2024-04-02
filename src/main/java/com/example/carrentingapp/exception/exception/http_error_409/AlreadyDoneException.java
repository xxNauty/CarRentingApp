package com.example.carrentingapp.exception.exception.http_error_409;

public final class AlreadyDoneException extends BaseConflictException {
    public AlreadyDoneException(String message) {
        super(message);
    }
}
