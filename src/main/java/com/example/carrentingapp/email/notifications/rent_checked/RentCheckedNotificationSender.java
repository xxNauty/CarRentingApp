package com.example.carrentingapp.email.notifications.rent_checked;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RentCheckedNotificationSender implements NotificationSender {

    private final EmailSender sender;

    @Override
    public void sendEmail(NotificationRequest request) {
        final String from = "contact@carrentingapp.pl";
        final String subject = "Your account has been locked";
        final String body = RentCheckedTemplate.template(((RentCheckedRequest) request).getRent().getStatus().name(), ((RentCheckedRequest) request).getRent().getDaysOfDelay());

        sender.send(((RentCheckedRequest) request).getRent().getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
