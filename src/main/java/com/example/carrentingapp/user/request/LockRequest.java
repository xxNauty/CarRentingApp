package com.example.carrentingapp.user.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class LockRequest implements Request {
    public Optional<String> userid;
    public Optional<String> lockType;
    public Optional<String> reason;
    public Optional<LocalDate> expirationDate;

    @Override
    public void checkInput() {
        if (this.userid.isPresent() && this.lockType.isPresent() && this.reason.isPresent()) {
            RequestValidationService.validateToken(this.userid.get());
            RequestValidationService.validateParameter(this.lockType.get());
            RequestValidationService.validateParameter(this.reason.get());
            this.expirationDate.ifPresent(RequestValidationService::validateParameter);
        } else {
            throw new InvalidArgumentException("You have to pass all values in order to lock user");
        }
    }
}
