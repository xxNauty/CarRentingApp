package com.example.carrentingapp.exception.exception.http_error_403;

public class UserTooYoungException extends BaseAccessDeniedException{
    public UserTooYoungException(String message) {
        super(message);
    }
}
