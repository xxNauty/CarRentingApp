package com.example.carrentingapp.exception.exception.http_error_403;

public class CarNotAvailableException extends BaseAccessDeniedException{
    public CarNotAvailableException(String message) {
        super(message);
    }
}
