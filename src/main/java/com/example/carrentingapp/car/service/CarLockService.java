package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.exception.exceptions.http_error_404.CarLockNotFoundException;
import com.example.carrentingapp.exception.exceptions.http_error_404.CarNotFoundException;
import com.example.carrentingapp.exception.exceptions.http_error_409.NotLockedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class CarLockService {

    private final CarBaseRepository baseCarRepository;

    private final CarLockRepository carLockRepository;

    public CarLockResponse lockCar(CarLockRequest request) {
        request.checkInput();
        CarBase car = baseCarRepository.findById(UUID.fromString(request.carId.get()))
                .orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        if (car.getStatus().equals(CarBase.CarStatus.CAR_LOCKED)) {
            CarLock lock = carLockRepository.findActiveLockForCar(car.getId())
                    .orElseThrow(() -> new CarLockNotFoundException("Car lock not found"));
            lock.setStatus(CarLock.CarLockStatus.CAR_LOCK_TIME_CHANGED);
            carLockRepository.save(lock);
        }

        CarLock lock = new CarLock(
                car,
                CarLock.CarLockReason.valueOf(request.reason.get()),
                request.lockedTo.get()
        );

        car.setStatus(CarBase.CarStatus.CAR_LOCKED);
        car.setUnavailableTo(lock.getLockedTo());

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now unavailable to rent");
    }

    public CarLockResponse unlockCar(CarUnlockRequest request) {
        request.checkInput();
        CarBase car = baseCarRepository.findById(UUID.fromString(request.carId.get()))
                .orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        CarLock lock = carLockRepository.findActiveLockForCar(
                UUID.fromString(request.carId.get())
        ).orElseThrow(() -> new NotLockedException("Car with given id is not locked"));

        lock.setStatus(CarLock.CarLockStatus.CAR_LOCK_NOT_ACTIVE);
        car.setStatus(CarBase.CarStatus.CAR_READY);
        car.setUnavailableTo(null);

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now available for renting again");
    }

}
