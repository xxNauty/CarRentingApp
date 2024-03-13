package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.exception.exception.http_error_404.CarLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarLockService {

    private final BaseCarRepository baseCarRepository;

    private final CarLockRepository carLockRepository;

    public CarLockResponse lockCar(CarLockRequest request){
        BaseCar car = baseCarRepository.findById(request.getCarId()).orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        CarLock lock = new CarLock(
                car,
                CarLock.CarReason.valueOf(request.getReason()),
                request.getLockedTo(),
                true
        );

        car.setIsAvailable(false);

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now unavailable to rent");
    }

    public CarLockResponse unlockCar(CarUnlockRequest request){
        BaseCar car = baseCarRepository.findById(request.getCarId()).orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        CarLock lock = carLockRepository.findAllActiveLocksForCar(request.getCarId()).orElseThrow(() -> new CarLockNotFoundException("Car with given id not found"));

        lock.setIsActive(false);
        car.setIsAvailable(true);

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now available for renting again");
    }

}
