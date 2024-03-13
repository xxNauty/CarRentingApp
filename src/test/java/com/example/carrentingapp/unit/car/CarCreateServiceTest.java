package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.response.MainCarResponse;
import com.example.carrentingapp.car.service.CarCreateService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarCreateServiceTest {

    @Autowired
    private CarCreateService carCreateService;

    @Autowired
    private BaseCarRepository carRepository;

    @Test
    public void testCreateCar(){
        MainCarResponse car = carCreateService.createCar(
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

        BaseCar carFromDatabase = carRepository.findById(car.getId()).orElseThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertFalse(carFromDatabase.getIsRented());
        Assertions.assertFalse(carFromDatabase.getHasActiveSale());
        Assertions.assertTrue(carFromDatabase.getIsAvailable());

    }

}
