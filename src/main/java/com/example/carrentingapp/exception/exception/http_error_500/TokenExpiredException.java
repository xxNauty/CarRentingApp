package com.example.carrentingapp.exception.exception.http_error_500;

public final class TokenExpiredException extends BaseInternalErrorException {
    public TokenExpiredException(String message) {
        super(message);
    }
}
