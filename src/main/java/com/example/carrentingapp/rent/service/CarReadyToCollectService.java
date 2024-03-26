package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.car_ready_to_collect.CarReadyToCollectRequest;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class CarReadyToCollectService {

    private final CarRentRepository carRentRepository;
    private final EmailNotificationSender notificationSender;

    public void check() {
        List<CarRent> rents = carRentRepository.getAllByStatus(CarRent.CarRentStatus.CAR_RENT_CAR_READY_TO_COLLECT);
        for(CarRent rent : rents){
            if (rent.getCollectionDate().toLocalDate().equals(LocalDate.now())){
                notificationSender.sendCarReadyToCollectNotification(new CarReadyToCollectRequest(rent.getUser()));
            }
        }
    }
}
