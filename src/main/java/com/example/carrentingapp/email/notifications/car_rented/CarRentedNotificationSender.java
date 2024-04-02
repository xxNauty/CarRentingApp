package com.example.carrentingapp.email.notifications.car_rented;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarRentedNotificationSender implements NotificationSender {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequest request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Car collected";
        final String body = CarRentedTemplate.template(((CarRentedRequest) request).getCar());

        sender.send(((CarRentedRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
