package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.util.Optional;

@AllArgsConstructor
public class CarRentRequest implements Request {


    public Optional<String> carId;
    public Optional<LocalDate> rentedFrom;
    public Optional<LocalDate> rentedTo;

    public void checkInput() {
        if (this.carId.isPresent() && this.rentedFrom.isPresent() && this.rentedTo.isPresent()) {
            RequestValidationService.validateToken(this.carId.get());
            RequestValidationService.validateParameter(this.rentedFrom.get());
            RequestValidationService.validateParameter(this.rentedTo.get());
        } else {
            throw new InvalidArgumentException("You have to pass all values in order to rent a car");
        }
    }
}
