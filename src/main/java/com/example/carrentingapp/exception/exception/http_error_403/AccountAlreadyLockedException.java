package com.example.carrentingapp.exception.exception.http_error_403;

public class AccountAlreadyLockedException extends BaseAccessDeniedException{
    public AccountAlreadyLockedException(String message) {
        super(message);
    }
}
