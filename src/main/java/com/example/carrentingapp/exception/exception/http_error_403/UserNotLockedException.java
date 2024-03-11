package com.example.carrentingapp.exception.exception.http_error_403;

public class UserNotLockedException extends BaseAccessDeniedException{
    public UserNotLockedException(String message) {
        super(message);
    }
}
