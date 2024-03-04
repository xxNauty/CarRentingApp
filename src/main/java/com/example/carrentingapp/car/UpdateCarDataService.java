package com.example.carrentingapp.car;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UpdateCarDataService {

    private final CarRepository carRepository;

    public void updateMileage(BaseCar car, float valueToAdd){
        car.setMileage(car.getMileage() + valueToAdd);

        carRepository.save(car);
    }

}
