package com.example.carrentingapp.email.notifications.car_ready_to_collect;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarReadyToCollectRequest implements NotificationRequestInterface {
    private BaseUser user;
}
