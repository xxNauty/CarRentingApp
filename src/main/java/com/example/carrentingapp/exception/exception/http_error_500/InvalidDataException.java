package com.example.carrentingapp.exception.exception.http_error_500;

public class InvalidDataException extends BaseInternalErrorException {

    public InvalidDataException(String message) {
        super(message);
    }
}