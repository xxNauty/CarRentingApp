package com.example.carrentingapp.exception;

public class InvalidEmailAddressException extends RuntimeException{
    public InvalidEmailAddressException(String message) {
        super(message);
    }
}
