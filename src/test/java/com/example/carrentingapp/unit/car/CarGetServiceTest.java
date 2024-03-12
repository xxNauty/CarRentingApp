package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.service.CarGetService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarGetServiceTest {

    @Autowired
    private CarGetService carGetService;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Test
    public void testGetCar(){
        UUID id = commonFunctionsProvider.createCarForTest();
        BaseCar car = carRepository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        GetCarResponse response = carGetService.getCarById(id);
        BaseCar carFromResponse = response.getCars();

        Assertions.assertEquals(car.getId(), carFromResponse.getId());
    }

    @Test
    public void testGetAllSimpleCars(){
        for (int i = 0; i < 5; i++){
            commonFunctionsProvider.createCarForTest();
        }

        List<CarGetService.SimpleCar> response = carGetService.getSimpleCarList(false).getCars();
        Assertions.assertEquals(5, response.size());
    }

    @Test
    public void testGetAllFullCars(){
        for (int i = 0; i < 5; i++){
            commonFunctionsProvider.createCarForTest();
        }

        List<BaseCar> response = carGetService.getFullCarList(false).getCars();
        Assertions.assertEquals(5, response.size());
    }

    @Test
    public void testGetAvailableSimpleCars(){
        for (int i = 0; i < 5; i++){
            commonFunctionsProvider.createCarForTest();
        }

        List<CarGetService.SimpleCar> response = carGetService.getSimpleCarList(true).getCars();
        Assertions.assertEquals(5, response.size());
    }

    @Test
    public void testGetAvailableFullCars(){
        for (int i = 0; i < 5; i++){
            commonFunctionsProvider.createCarForTest();
        }

        List<BaseCar> response = carGetService.getFullCarList(true).getCars();
        Assertions.assertEquals(5, response.size());
    }


}
