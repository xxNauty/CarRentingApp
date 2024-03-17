package com.example.carrentingapp.email.notifications.car_rented;

import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarRentedNotificationSender implements NotificationSenderInterface {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequestInterface request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Car collected";
        final String body = CarRentedTemplate.template(((CarRentedRequest) request).getCar());

        sender.send(((CarRentedRequest) request).getUser().getEmail(), from, subject, body);
    }
}
