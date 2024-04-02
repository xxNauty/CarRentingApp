package com.example.carrentingapp.email.notifications.car_returned;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarReturnedNotificationSender implements NotificationSender {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequest request) {
        final String from = "password.reset@carrentingapp.pl";
        final String subject = "Reset your password";
        final String body = CarReturnedTemplate.template();

        sender.send(((CarReturnedRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
