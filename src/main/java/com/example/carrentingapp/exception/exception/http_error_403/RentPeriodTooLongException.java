package com.example.carrentingapp.exception.exception.http_error_403;

public class RentPeriodTooLongException extends BaseAccessDeniedException{
    public RentPeriodTooLongException(String message) {
        super(message);
    }
}
