package com.example.carrentingapp.car.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class CarUpdateMileageRequest implements Request {
    public Optional<String> carId;
    public Optional<Float> mileageToAdd;

    @Override
    public void checkInput() {
        if (this.carId.isPresent() && this.mileageToAdd.isPresent()) {
            RequestValidationService.validateToken(this.carId.get());
            RequestValidationService.validateParameter(this.mileageToAdd.get());
        } else {
            throw new InvalidArgumentException("You have to pass both car id and value of kilometers to add in order to update car's mileage");
        }
    }
}
