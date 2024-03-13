package com.example.carrentingapp.exception.exception.http_error_403;

public class TokenExpiredException extends BaseAccessDeniedException{
    public TokenExpiredException(String message) {
        super(message);
    }
}
