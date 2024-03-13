package com.example.carrentingapp.email_verification.notifications.confirm_email;

import com.example.carrentingapp.email_verification.sender.EmailSender;
import com.example.carrentingapp.email_verification.token.ConfirmationTokenService;
import com.example.carrentingapp.user.BaseUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConfirmEmailNotificationSender {

    private final EmailSender sender;
    private final ConfirmationTokenService confirmationTokenService;

    public void sendConfirmationEmail(BaseUser user){
        final String subject = "Confirm your email address";
        final String token = confirmationTokenService.createToken(user);

        sender.send(user.getEmail(), subject, ConfirmEmailTemplate.template(token));

    }
}
