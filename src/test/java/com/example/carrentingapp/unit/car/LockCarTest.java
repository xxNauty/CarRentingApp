package com.example.carrentingapp.unit.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.car.service.CarGetService;
import com.example.carrentingapp.car.service.CarLockService;
import com.example.carrentingapp.exception.exception.http_error_404.CarLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LockCarTest {

    @Autowired
    private CarLockService carLockService;

    @Autowired
    private BaseCarRepository carRepository;

    @Autowired
    private CarLockRepository carLockRepository;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Test
    public void testLockCar(){
        UUID carId = commonFunctionsProvider.createCarForTest();

        CarLockResponse response = carLockService.lockCar(
                new CarLockRequest(
                        carId,
                        CarLock.CarReason.TUNING.name(),
                        (LocalDate.now()).plusDays(23)
                )
        );
        Assertions.assertEquals("Car is now unavailable to rent", response.getMessage());

        BaseCar car = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException("Car not found"));
        CarLock carLock = carLockRepository.findAllActiveLocksForCar(carId).orElseThrow(() -> new CarLockNotFoundException("Car lock not found"));

        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertDoesNotThrow(() -> new CarLockNotFoundException("Car lock not found"));

        Assertions.assertFalse(car.getIsAvailable());
        Assertions.assertTrue(carLock.getIsActive());
        Assertions.assertEquals(CarLock.CarReason.TUNING, carLock.getReason());
    }

    @Test
    public void testUnlockCar(){
        UUID carId = commonFunctionsProvider.createCarForTest();

        carLockService.lockCar(
                new CarLockRequest(
                        carId,
                        CarLock.CarReason.TUNING.name(),
                        (LocalDate.now()).plusDays(23)
                )
        );

        BaseCar carBeforeUnlock = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException("Car not found"));

        Assertions.assertFalse(carBeforeUnlock.getIsAvailable());

        CarLockResponse response = carLockService.unlockCar(
                new CarUnlockRequest(carId)
        );

        Assertions.assertEquals("Car is now available for renting again", response.getMessage());

        BaseCar carAfterUnlock = carRepository.findById(carId).orElseThrow(() -> new CarNotFoundException("Car not found"));
        CarLock carLock = carLockRepository.findAllActiveLocksForCar(carId).orElse(new CarLock());

        Assertions.assertDoesNotThrow(() -> new CarNotFoundException("Car not found"));
        Assertions.assertEquals(new CarLock(), carLock);

        Assertions.assertTrue(carAfterUnlock.getIsAvailable());
    }
}
