package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class CarCheckAfterRentRequest implements Request {

    public Optional<String> carRentId;
    public Optional<Boolean> isDamaged;

    @Override
    public void checkInput() {
        if (this.carRentId.isPresent() && this.isDamaged.isPresent()) {
            RequestValidationService.validateToken(this.carRentId.get());
            RequestValidationService.validateParameter(this.isDamaged.get());
        } else {
            throw new InvalidArgumentException("You have to pass both values in order to verify car after rent");
        }
    }
}
