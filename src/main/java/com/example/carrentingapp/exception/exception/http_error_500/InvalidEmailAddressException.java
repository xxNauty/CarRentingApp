package com.example.carrentingapp.exception.exception.http_error_500;

public class InvalidEmailAddressException extends BaseInternalErrorException {

    public InvalidEmailAddressException(String message) {
        super(message);
    }
}
