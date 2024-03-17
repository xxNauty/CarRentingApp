package com.example.carrentingapp.email.notifications.car_collected;

import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedRequest;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedTemplate;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CarCollectedNotificationSender implements NotificationSenderInterface {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequestInterface request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Car collected";
        final String body = CarCollectedTemplate.template(((CarCollectedRequest) request).getRent());

        sender.send(((CarCollectedRequest) request).getRent().getUser().getEmail(), from, subject, body);
    }
}
