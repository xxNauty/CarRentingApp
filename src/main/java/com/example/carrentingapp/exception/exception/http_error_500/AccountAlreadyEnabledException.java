package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class AccountAlreadyEnabledException extends BaseInternalErrorException {
    public AccountAlreadyEnabledException(String message) {
        super(message);
    }
}
