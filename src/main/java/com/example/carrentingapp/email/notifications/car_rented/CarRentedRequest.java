package com.example.carrentingapp.email.notifications.car_rented;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarRentedRequest implements NotificationRequest {
    private CarBase car;
    private UserBase user;
}
