package com.example.carrentingapp.exception.exception.http_error_500;

public final class CarAlreadyReturnedException extends BaseInternalErrorException{
    public CarAlreadyReturnedException(String message) {
        super(message);
    }
}
