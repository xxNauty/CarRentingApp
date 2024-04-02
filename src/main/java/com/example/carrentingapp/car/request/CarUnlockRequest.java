package com.example.carrentingapp.car.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class CarUnlockRequest implements Request {

    public Optional<String> carId;

    @Override
    public void checkInput() {
        if (this.carId.isPresent()) {
            RequestValidationService.validateToken(this.carId.get());
        } else {
            throw new InvalidArgumentException("You have to pass id of car in order to lock it");
        }
    }
}
