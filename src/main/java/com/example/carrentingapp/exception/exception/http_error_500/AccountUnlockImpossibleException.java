package com.example.carrentingapp.exception.exception.http_error_500;

import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;

public class AccountUnlockImpossibleException extends BaseInternalErrorException {
    public AccountUnlockImpossibleException(String message) {
        super(message);
    }
}
