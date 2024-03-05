package com.example.carrentingapp.car;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CarMileageUpdaterTest {

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UpdateCarDataService carDataService;

    @Autowired
    private CarCreatorService carCreatorService;

    @Test
    void carMileageUpdater(){
        BaseCar car = carCreatorService.createCar(
                "Opel",
                "Corsa",
                2022,
                0F,
                105.3F,
                123F,
                1.2F,
                6.7F,
                6.0F,
                100F
        );

        Assertions.assertEquals(car.getMileage(), 0F);

        carDataService.updateMileage(car, 50.5F);

        Assertions.assertEquals(car.getMileage(), 50.5F);

        carDataService.updateMileage(car, 21.43F);

        Assertions.assertEquals(car.getMileage(), 71.93F);
    }
}
