package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
public class CarCollectRequest implements Request {
    public Optional<String> carRentId;

    @Override
    public void checkInput() {
        if(this.carRentId.isPresent()){
            RequestValidationService.validateToken(this.carRentId.get());
        }
        else {
            throw new InvalidArgumentException("You have to pass id of car you want to collect");
        }
    }
}
