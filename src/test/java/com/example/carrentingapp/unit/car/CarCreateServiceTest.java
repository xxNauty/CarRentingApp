package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.MainCarResponse;
import com.example.carrentingapp.car.service.CarCreateService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarCreateServiceTest {

    @Autowired
    private CarCreateService service;

    @Autowired
    private CarRepository repository;

    @Test
    public void testCreateCar(){
        MainCarResponse car = service.createCar(
                "Opel",
                "Corsa",
                2000,
                300_000F,
                50F,
                90F,
                1F,
                5.6F,
                5F,
                430.99F
        );

        BaseCar carFromDatabase = repository.findById(car.getId()).orElseThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertFalse(carFromDatabase.getIsRented());
        Assertions.assertFalse(carFromDatabase.getHasActiveSale());
        Assertions.assertTrue(carFromDatabase.getIsAvailable());

    }

}
