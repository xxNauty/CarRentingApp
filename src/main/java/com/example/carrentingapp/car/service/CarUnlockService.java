package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.user.UserLock;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CarUnlockService {

    private final CarLockRepository carLockRepository;
    private final CarLockService carLockService;

    public void check(){
        List<CarLock> locks = carLockRepository.findAllByStatus(CarLock.CarLockStatus.CAR_LOCK_ACTIVE);
        for (CarLock lock : locks){
            if (lock.getLockedTo().equals(LocalDate.now())){
                carLockService.unlockCar(new CarUnlockRequest(Optional.of(lock.getId().toString())));
            }
        }
    }

}
