package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.exception.exception.http_error_404.CarLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_500.CarNotLockedException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class CarLockService {

    private final CarBaseRepository baseCarRepository;

    private final CarLockRepository carLockRepository;

    public CarLockResponse lockCar(CarLockRequest request){
        CarBase car = baseCarRepository.findById(request.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        if(car.getStatus().equals(CarBase.CarStatus.CAR_LOCKED)){
            CarLock lock = carLockRepository.findActiveLockForCar(car.getId())
                    .orElseThrow(() -> new CarLockNotFoundException("Car lock not found"));
            lock.setStatus(CarLock.CarLockStatus.CAR_LOCK_EXTENDED);
            carLockRepository.save(lock);
        }

        CarLock lock = new CarLock(
                car,
                CarLock.CarLockReason.valueOf(request.getReason()),
                request.getLockedTo()
        );

        car.setStatus(CarBase.CarStatus.CAR_LOCKED);

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now unavailable to rent");
    }

    public CarLockResponse unlockCar(CarUnlockRequest request){
        CarBase car = baseCarRepository.findById(request.getCarId()).orElseThrow(() -> new CarNotFoundException("Car with given id not found"));

        CarLock lock = carLockRepository.findActiveLockForCar(
                request.getCarId()
        ).orElseThrow(() -> new CarNotLockedException("Car with given id is not locked"));

        lock.setStatus(CarLock.CarLockStatus.CAR_LOCK_NOT_ACTIVE);
        car.setStatus(CarBase.CarStatus.CAR_READY);

        baseCarRepository.save(car);
        carLockRepository.save(lock);

        return new CarLockResponse("Car is now available for renting again");
    }

}
