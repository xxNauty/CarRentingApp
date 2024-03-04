package com.example.carrentingapp.car;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class CarCreationTest {

    @Autowired
    private CarCreatorService carCreatorService;

    @Test
    void CarCreatorService(){
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

        Assertions.assertInstanceOf(UUID.class, car.getId());
        Assertions.assertFalse(car.getIsRented());
        Assertions.assertFalse(car.getHasActiveSale());

    }

}
