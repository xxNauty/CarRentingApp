package com.example.carrentingapp.authentication.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class SendVerifyingTokenAgainRequest implements Request {
    public Optional<String> email;
    public Optional<String> userId;

    @Override
    public void checkInput() {
        if(this.email.isPresent() && this.userId.isPresent()){
            RequestValidationService.validateParameter(this.email.get());
            RequestValidationService.validateToken(this.userId.get());
        }
        else {
            throw new InvalidArgumentException("You have to pass all values to receive token to reset password");
        }
    }
}
