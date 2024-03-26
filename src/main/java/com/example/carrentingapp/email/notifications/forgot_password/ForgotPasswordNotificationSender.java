package com.example.carrentingapp.email.notifications.forgot_password;

import com.example.carrentingapp.email.message_history.EmailMessage;
import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationTokenService;
import com.example.carrentingapp.email.sender.EmailSender;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ForgotPasswordNotificationSender implements NotificationSender {

    private final EmailSender sender;
    private final ForgotPasswordVerificationTokenService forgotPasswordVerificationTokenService;

    @Override
    public void sendEmail(NotificationRequest request) {
        final String from = "password.reset@carrentingapp.pl";
        final String subject = "Reset your password";
        final String token = forgotPasswordVerificationTokenService.createToken(((ForgotPasswordRequest) request).getUser());
        final String body = ForgotPasswordTemplate.template(token);

        sender.send(((ForgotPasswordRequest) request).getUser().getEmail(), from, subject, body, EmailMessage.EmailMessageType.NOTIFICATION);
    }
}
