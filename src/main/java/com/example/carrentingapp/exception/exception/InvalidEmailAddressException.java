package com.example.carrentingapp.exception.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class InvalidEmailAddressException extends BaseCustomException{

    public InvalidEmailAddressException(String message) {
        super(message);
    }
}
