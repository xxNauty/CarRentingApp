package com.example.carrentingapp.email.notifications.account_unlocked;

import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountUnlockedNotificationSender implements NotificationSenderInterface {

    private final EmailSender sender;
    @Override
    public void sendEmail(NotificationRequestInterface request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Your account has been unlocked";
        final String body = AccountUnlockedTemplate.template();

        sender.send(((AccountUnlockedRequest) request).getUser().getEmail(), from, subject, body);
    }
}
