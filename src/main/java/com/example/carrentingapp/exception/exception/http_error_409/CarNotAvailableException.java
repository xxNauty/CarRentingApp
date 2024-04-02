package com.example.carrentingapp.exception.exception.http_error_409;

import com.example.carrentingapp.exception.exception.http_error_500.BaseInternalErrorException;

public final class CarNotAvailableException extends BaseInternalErrorException {
    public CarNotAvailableException(String message) {
        super(message);
    }
}
