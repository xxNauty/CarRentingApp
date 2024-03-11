package com.example.carrentingapp.exception.exception.http_error_403;

public class UserAccountLockedException extends BaseAccessDeniedException{
    public UserAccountLockedException(String message) {
        super(message);
    }
}
