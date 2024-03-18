package com.example.carrentingapp.email.notifications.confirm_email;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationSenderInterface;
import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.email.sender.EmailSender;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ConfirmEmailNotificationSender implements NotificationSenderInterface {

    private final EmailSender sender;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public void sendEmail(NotificationRequestInterface request){
        final String from = "hello@carrentingapp.pl";
        final String subject = "Confirm your email address";
        final String token = confirmationTokenService.createToken(((ConfirmEmailRequest) request).getUser());
        final String body = ConfirmEmailTemplate.template(token);

        sender.send(((ConfirmEmailRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
