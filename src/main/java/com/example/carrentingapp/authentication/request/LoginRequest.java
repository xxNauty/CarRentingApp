package com.example.carrentingapp.authentication.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.*;

import java.util.Optional;

@AllArgsConstructor
public class LoginRequest implements Request {

    public Optional<String> email;
    public Optional<String> password;

    @Override
    public void checkInput() {
        if(email.isPresent() && password.isPresent()){
            RequestValidationService.validateParameter(this.email.get());
            RequestValidationService.validateParameter(this.password.get());
        }
        else{
            throw new InvalidArgumentException("You have to pass both email and password to log in");
        }
    }
}
