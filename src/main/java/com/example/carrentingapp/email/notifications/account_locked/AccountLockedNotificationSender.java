package com.example.carrentingapp.email.notifications.account_locked;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AccountLockedNotificationSender implements NotificationSenderInterface {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequestInterface request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Your account has been locked";
        final String body = AccountLockedTemplate.template(((AccountLockedRequest) request).getLock());

        sender.send(((AccountLockedRequest) request).getLock().getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
