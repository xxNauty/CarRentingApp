package com.example.carrentingapp.email.notifications.car_collected;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.rent.CarRent;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarCollectedRequest implements NotificationRequestInterface {
    private CarRent rent;
}