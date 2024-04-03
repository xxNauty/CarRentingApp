package com.example.carrentingapp.authentication.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
public class RegistrationRequest implements Request {

    public Optional<String> firstName;
    public Optional<String> lastName;
    public Optional<String> email;
    public Optional<String> password;
    public Optional<LocalDate> dateOfBirth;

    @Override
    public void checkInput() {
        if (this.firstName.isPresent() && this.lastName.isPresent() &&
                this.email.isPresent() && this.password.isPresent() && this.dateOfBirth.isPresent()) {
            RequestValidationService.validateName(this.firstName.get());
            RequestValidationService.validateName(this.lastName.get());
            RequestValidationService.validateEmail(this.email.get());
            RequestValidationService.validatePassword(this.password.get());
            RequestValidationService.validateAge(this.dateOfBirth.get());
        } else {
            throw new InvalidArgumentException("You have to pass all values in order to register your account");
        }
    }
}
