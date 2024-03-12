package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.response.GetSimpleCarListResponse;
import com.example.carrentingapp.car.response.MainCarResponse;
import com.example.carrentingapp.car.service.CarCreateService;
import com.example.carrentingapp.car.service.CarGetService;
import com.example.carrentingapp.car.service.CarUpdateService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DataJdbcTest
public class CarGetServiceTest {

    @Autowired
    private CarCreateService carService;

    @Autowired
    private CarGetService service;

    @Autowired
    private CarRepository repository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void execute() {
        jdbcTemplate.execute("TRUNCATE TABLE base_car" );
    }

    private UUID createCarForTest(){
        MainCarResponse response =  carService.createCar(
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

        return response.getId();
    }

    @Test
    public void testGetCar(){
        UUID id = createCarForTest();
        BaseCar car = repository.findById(id).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        GetCarResponse response = service.getCarById(id);
        BaseCar carFromResponse = response.getCars();

        Assertions.assertEquals(car.getId(), carFromResponse.getId());
    }

    @Test
    public void testGetAllSimpleCars(){
        for (int i = 0; i < 5; i++){
            createCarForTest();
        }

        List<CarGetService.SimpleCar> response = service.getSimpleCarList(false).getCarList();
        Assertions.assertEquals(5, response.size());

    }

}
