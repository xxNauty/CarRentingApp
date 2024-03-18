package com.example.carrentingapp.email.notifications.car_rented;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarRentedRequest implements NotificationRequestInterface {
    private BaseCar car;
    private BaseUser user;
}
