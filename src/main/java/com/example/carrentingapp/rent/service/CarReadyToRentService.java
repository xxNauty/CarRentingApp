package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.car_ready_to_collect.CarReadyToCollectRequest;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CarReadyToRentService{

    private final CarRentRepository carRentRepository;
    private final NotificationSender notificationSender;

    public void check() {
        List<CarRent> rents = carRentRepository.getAllByCollectedCar(false);
        for(CarRent rent : rents){
            if (rent.getCollectionDate().toLocalDate().equals(LocalDate.now())){
                notificationSender.sendCarReadyToCollectNotification(new CarReadyToCollectRequest(rent.getUser()));
            }
        }
    }
}
