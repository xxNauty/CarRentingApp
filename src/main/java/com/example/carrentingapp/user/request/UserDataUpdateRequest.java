package com.example.carrentingapp.user.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class UserDataUpdateRequest implements Request {
    public Optional<String> userId;
    public Optional<String> firstName;
    public Optional<String> lastName;
    public Optional<String> email;
    public Optional<LocalDate> dateOfBirth;

    @Override
    public void checkInput() {
        if (this.userId.isPresent()) {
            RequestValidationService.validateToken(this.userId.get());
            this.firstName.ifPresent(RequestValidationService::validateName);
            this.lastName.ifPresent(RequestValidationService::validateName);
            this.email.ifPresent(RequestValidationService::validateEmail);
            this.dateOfBirth.ifPresent(RequestValidationService::validateAge);
        } else {
            throw new InvalidArgumentException("You have to pass id of user you want to update");
        }

    }
}

