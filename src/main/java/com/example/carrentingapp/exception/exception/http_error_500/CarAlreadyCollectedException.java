package com.example.carrentingapp.exception.exception.http_error_500;

public class CarAlreadyCollectedException extends BaseInternalErrorException{
    public CarAlreadyCollectedException(String message) {
        super(message);
    }
}
