package com.example.carrentingapp.email.notifications.car_returned;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.notifications.NotificationSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarReturnedNotificationSender implements NotificationSender {
    @Override
    public void sendEmail(NotificationRequest request) {

    }
}
