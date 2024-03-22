package com.example.carrentingapp.exception.exception.http_error_500;

public class CarNotLockedException extends BaseInternalErrorException{
    public CarNotLockedException(String message) {
        super(message);
    }
}
