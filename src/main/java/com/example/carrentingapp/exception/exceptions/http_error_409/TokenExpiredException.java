package com.example.carrentingapp.exception.exceptions.http_error_409;

public final class TokenExpiredException extends BaseConflictException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
