package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class UserNotLockedException extends BaseInternalErrorException {
    public UserNotLockedException(String message) {
        super(message);
    }
}
