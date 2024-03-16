package com.example.carrentingapp.exception.exception.http_error_403;

public class AccountAlreadyEnabledException extends BaseAccessDeniedException{
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
