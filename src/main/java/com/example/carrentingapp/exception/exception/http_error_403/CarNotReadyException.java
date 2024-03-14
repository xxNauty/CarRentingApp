package com.example.carrentingapp.exception.exception.http_error_403;

public class CarNotReadyException extends BaseAccessDeniedException{
    public CarNotReadyException(String message) {
        super(message);
    }
}
