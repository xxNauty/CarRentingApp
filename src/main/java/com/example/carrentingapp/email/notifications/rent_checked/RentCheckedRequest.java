package com.example.carrentingapp.email.notifications.rent_checked;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.rent.CarRent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RentCheckedRequest implements NotificationRequest {
    private CarRent rent;
}
