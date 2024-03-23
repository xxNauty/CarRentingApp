package com.example.carrentingapp.email.notifications.car_ready_to_collect;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarReadyToCollectRequest implements NotificationRequestInterface {
    private UserBase user;
}
