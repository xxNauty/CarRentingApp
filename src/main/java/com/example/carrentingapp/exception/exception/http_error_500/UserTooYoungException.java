package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class UserTooYoungException extends BaseInternalErrorException {
    public UserTooYoungException(String message) {
        super(message);
    }
}
