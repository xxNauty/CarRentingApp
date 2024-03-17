package com.example.carrentingapp.email.notifications.car_returned;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarReturnedNotificationSender implements NotificationSenderInterface {
    @Override
    public void sendEmail(NotificationRequestInterface request) {

    }
}
