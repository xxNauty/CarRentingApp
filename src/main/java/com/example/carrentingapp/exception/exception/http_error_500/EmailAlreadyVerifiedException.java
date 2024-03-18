package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class EmailAlreadyVerifiedException extends BaseInternalErrorException {
    public EmailAlreadyVerifiedException(String message) {
        super(message);
    }
}
