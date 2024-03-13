package com.example.carrentingapp.exception.exception.http_error_403;

public class EmailAlreadyVerifiedException extends BaseAccessDeniedException{
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
