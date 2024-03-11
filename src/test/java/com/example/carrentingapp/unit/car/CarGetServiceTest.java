package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.GetCarListResponse;
import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.service.CarGetService;
import com.example.carrentingapp.car.service.CarUpdateService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarGetServiceTest {

    @Autowired
    private CarGetService service;

    @Autowired
    private CarRepository repository;

    @Autowired
    private CommonFunctionsProvider provider;

    @Test
    public void testGetCar(){
        UUID id = provider.createCarForTest();
        BaseCar car = repository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        GetCarResponse response = service.getCarById(id);
        BaseCar carFromResponse = response.getCars();

        Assertions.assertEquals(car.getId(), carFromResponse.getId());
    }

    public void testGetAllCars(){
        GetCarListResponse response = service.getAllCars();
        for (int i = 0; i < response.getCars().size(); i++) {

        }
    }

}
