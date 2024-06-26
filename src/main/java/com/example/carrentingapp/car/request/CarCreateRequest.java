package com.example.carrentingapp.car.request;

import com.example.carrentingapp.authentication.service.RequestValidationService;
import com.example.carrentingapp.configuration.common_interfaces.Request;
import com.example.carrentingapp.exception.exceptions.http_error_500.InvalidArgumentException;
import lombok.AllArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
public class CarCreateRequest implements Request {
    public Optional<String> brand;
    public Optional<String> model;
    public Optional<Integer> yearOfProduction;
    public Optional<Float> mileage;
    public Optional<Float> power;
    public Optional<Float> torque;
    public Optional<Float> engineSize;
    public Optional<Float> averageFuelConsumption;
    public Optional<Float> minRankOfUser;
    public Optional<Float> pricePerDay;

    @Override
    public void checkInput() {
        if (this.brand.isPresent() && this.model.isPresent() && this.yearOfProduction.isPresent() &&
                this.mileage.isPresent() && this.power.isPresent() && this.torque.isPresent() &&
                this.engineSize.isPresent() && this.averageFuelConsumption.isPresent() &&
                this.minRankOfUser.isPresent() && this.pricePerDay.isPresent()
        ) {
            RequestValidationService.validateParameter(this.brand.get());
            RequestValidationService.validateParameter(this.model.get());
            RequestValidationService.validateParameter(this.yearOfProduction.get());
            RequestValidationService.validateParameter(this.mileage.get());
            RequestValidationService.validateParameter(this.power.get());
            RequestValidationService.validateParameter(this.torque.get());
            RequestValidationService.validateParameter(this.engineSize.get());
            RequestValidationService.validateParameter(this.averageFuelConsumption.get());
            RequestValidationService.validateParameter(this.minRankOfUser.get());
            RequestValidationService.validateParameter(this.pricePerDay.get());
        } else {
            throw new InvalidArgumentException("You have to pass all values in order to create new car");
        }
    }
}
