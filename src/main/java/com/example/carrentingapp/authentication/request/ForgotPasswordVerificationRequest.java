package com.example.carrentingapp.authentication.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordVerificationRequest implements Request {

    public Optional<String> email;

    @Override
    public void checkInput() {
        if (this.email.isPresent()) {
            RequestValidationService.validateEmail(this.email.get());
        } else {
            throw new InvalidArgumentException("You have to pass an email in order to reset your password");
        }

    }
}
