package com.example.carrentingapp.email.notifications.confirm_email;

import com.example.carrentingapp.email.sender.EmailSender;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenService;
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

        sender.send(user.getEmail(), "hello@carrentingapp.pl", subject, ConfirmEmailTemplate.template(token));

    }
}
