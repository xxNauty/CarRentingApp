package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class RentPeriodTooLongException extends BaseInternalErrorException {
    public RentPeriodTooLongException(String message) {
        super(message);
    }
}
