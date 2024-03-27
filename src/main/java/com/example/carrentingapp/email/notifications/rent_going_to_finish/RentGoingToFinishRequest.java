package com.example.carrentingapp.email.notifications.rent_going_to_finish;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentGoingToFinishRequest implements NotificationRequest {
    private UserBase user;
}
