package com.example.carrentingapp.email.notifications.car_returned;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarReturnedRequest implements NotificationRequest {
    private UserBase user;
}
