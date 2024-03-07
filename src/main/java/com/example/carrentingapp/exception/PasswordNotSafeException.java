package com.example.carrentingapp.exception;

public class PasswordNotSafeException extends RuntimeException{
    public PasswordNotSafeException(String message) {
        super(message);
    }
}
