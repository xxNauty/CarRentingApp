package com.example.carrentingapp.email.notifications.confirm_email;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.sender.EmailSender;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConfirmEmailNotificationSender implements NotificationSender {

    private final EmailSender sender;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public void sendEmail(NotificationRequest request){
        final String from = "hello@carrentingapp.pl";
        final String subject = "Confirm your email address";
        final String token = confirmationTokenService.createToken(((ConfirmEmailRequest) request).getUser());
        final String body = ConfirmEmailTemplate.template(token);

        sender.send(((ConfirmEmailRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
