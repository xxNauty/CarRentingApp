package com.example.carrentingapp.email.notifications.car_ready_to_collect;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarReadyToCollectNotificationSender implements NotificationSender {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequest request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Car collected";
        final String body = CarReadyToCollectTemplate.template();

        sender.send(((CarReadyToCollectRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
