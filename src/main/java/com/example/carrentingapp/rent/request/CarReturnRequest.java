package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
public class CarReturnRequest implements Request {
    public Optional<String> carRentId;
    public Optional<Float> kilometersTraveled;

    @Override
    public void checkInput() {
        if (this.carRentId.isPresent() && this.kilometersTraveled.isPresent()){
            RequestValidationService.validateToken(this.carRentId.get());
            RequestValidationService.validateParameter(this.kilometersTraveled.get());
        }
        else {
            throw new InvalidArgumentException("You have to pass both values in order to return car");
        }
    }
}
