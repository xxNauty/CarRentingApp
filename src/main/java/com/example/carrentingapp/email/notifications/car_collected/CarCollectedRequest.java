package com.example.carrentingapp.email.notifications.car_collected;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.rent.CarRent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarCollectedRequest implements NotificationRequest {
    private CarRent rent;
}
