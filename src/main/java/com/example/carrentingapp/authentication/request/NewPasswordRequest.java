package com.example.carrentingapp.authentication.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class NewPasswordRequest implements Request {
    public Optional<String> email;
    public Optional<String> token;
    public Optional<String> password;

    @Override
    public void checkInput() {
        if (this.email.isPresent() && this.token.isPresent() && this.password.isPresent()) {
            RequestValidationService.validateParameter(this.email.get());
            RequestValidationService.validateToken(this.token.get());
            RequestValidationService.validatePassword(this.password.get());
        } else {
            throw new InvalidArgumentException("You have to pass all values in order to set new password");
        }
    }
}
