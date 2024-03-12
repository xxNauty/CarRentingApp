package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.request.UpdateCarDataRequest;
import com.example.carrentingapp.car.service.CarUpdateService;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CarUpdateServiceTest {

    @Autowired
    private CarUpdateService service;

    @Autowired
    private CarRepository repository;

    @Autowired
    private CommonFunctionsProvider provider;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void execute() {
        jdbcTemplate.execute("TRUNCATE TABLE base_car" );
    }

    @Test
    public void testUpdateCarMileage(){
        BaseCar car = repository.findById(provider.createCarForTest()).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        Float valueToAdd = 123.45F;

        Float mileageBefore = car.getMileage();
        service.updateMileageFromRequest(car, valueToAdd);
        Float mileageAfter = car.getMileage();

        Assertions.assertEquals(mileageBefore + valueToAdd, mileageAfter);
    }

    @Test
    public void testUpdateCar(){
        BaseCar car = repository.findById(provider.createCarForTest()).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        service.updateCarDataResponse(new UpdateCarDataRequest(
                car.getId(),
                car.getBrand(),
                "Astra",
                car.getYearOfProduction(),
                car.getPower(),
                300F,
                car.getEngineSize(),
                7.5F,
                car.getMinRankOfUser(),
                800.50F
        ));

        BaseCar carFromDatabase = repository.findById(car.getId()).orElseThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertEquals(car.getId(), carFromDatabase.getId());
        Assertions.assertEquals(car.getBrand(), carFromDatabase.getBrand());
        Assertions.assertNotEquals(car.getModel(), carFromDatabase.getModel());
        Assertions.assertNotEquals(car.getTorque(), carFromDatabase.getTorque());
        Assertions.assertNotEquals(car.getAverageFuelConsumption(), carFromDatabase.getAverageFuelConsumption());
        Assertions.assertNotEquals(car.getPricePerDay(), carFromDatabase.getPricePerDay());

    }
}
