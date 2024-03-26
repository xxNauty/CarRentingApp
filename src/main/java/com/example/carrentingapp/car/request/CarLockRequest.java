package com.example.carrentingapp.car.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.*;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
public class CarLockRequest implements Request {
    public Optional<String> carId;
    public Optional<String> reason;
    public Optional<LocalDate> lockedTo;

    @Override
    public void checkInput() {
        if(this.carId.isPresent() && this.reason.isPresent() && this.lockedTo.isPresent()){
            RequestValidationService.validateToken(this.carId.get());
            RequestValidationService.validateParameter(this.reason.get());
            RequestValidationService.validateParameter(this.lockedTo.get());
        }
        else{
            throw new InvalidArgumentException("You have to pass all values in order to lock car");
        }
    }
}
