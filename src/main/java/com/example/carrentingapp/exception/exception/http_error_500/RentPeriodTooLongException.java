package com.example.carrentingapp.exception.exception.http_error_500;

public final class RentPeriodTooLongException extends BaseInternalErrorException {
    public RentPeriodTooLongException(String message) {
        super(message);
    }
}
