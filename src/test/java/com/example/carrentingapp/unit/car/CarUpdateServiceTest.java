package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.request.UpdateCarDataRequest;
import com.example.carrentingapp.car.service.CarUpdateService;
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
public class CarUpdateServiceTest {

    @Autowired
    private CarUpdateService carUpdateService;

    @Autowired
    private BaseCarRepository carRepository;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Test
    public void testUpdateCarMileage(){
        BaseCar car = carRepository.findById(commonFunctionsProvider.createCarForTest()).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        Float valueToAdd = 123.45F;

        Float mileageBefore = car.getMileage();
        carUpdateService.updateMileageFromRequest(car, valueToAdd);
        Float mileageAfter = car.getMileage();

        Assertions.assertEquals(mileageBefore + valueToAdd, mileageAfter);
    }

    @Test
    public void testUpdateCar(){
        BaseCar car = carRepository.findById(commonFunctionsProvider.createCarForTest()).orElseThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));

        carUpdateService.updateCarDataResponse(new UpdateCarDataRequest(
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

        BaseCar carFromDatabase = carRepository.findById(car.getId()).orElseThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertEquals(car.getId(), carFromDatabase.getId());
        Assertions.assertEquals(car.getBrand(), carFromDatabase.getBrand());
        Assertions.assertNotEquals(car.getModel(), carFromDatabase.getModel());
        Assertions.assertNotEquals(car.getTorque(), carFromDatabase.getTorque());
        Assertions.assertNotEquals(car.getAverageFuelConsumption(), carFromDatabase.getAverageFuelConsumption());
        Assertions.assertNotEquals(car.getPricePerDay(), carFromDatabase.getPricePerDay());

    }
}
