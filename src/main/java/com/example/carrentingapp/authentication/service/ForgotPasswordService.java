package com.example.carrentingapp.authentication.service;

import com.example.carrentingapp.authentication.request.ForgotPasswordVerificationRequest;
import com.example.carrentingapp.authentication.request.NewPasswordRequest;
import com.example.carrentingapp.authentication.response.ForgotPasswordResponse;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.forgot_password.ForgotPasswordRequest;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationToken;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationTokenRepository;
import com.example.carrentingapp.exception.exception.http_error_403.AccessDeniedException;
import com.example.carrentingapp.exception.exception.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.service.PasswordService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ForgotPasswordService {

    private final BaseUserRepository baseUserRepository;
    private final NotificationSender notificationSender;
    private final ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;
    private final PasswordService passwordService;

    public ForgotPasswordResponse sendEmail(ForgotPasswordVerificationRequest request){
        BaseUser user = baseUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UserNotFoundException("There is no user with given Id"));
        notificationSender.sendForgotPasswordNotification(new ForgotPasswordRequest(user));
        return new ForgotPasswordResponse("An email sent, check your inbox and follow the instructions given in message");
    }

    public ForgotPasswordResponse changePassword(NewPasswordRequest request){
        ForgotPasswordVerificationToken token = forgotPasswordVerificationTokenRepository
                .findByToken(request.getToken()).orElseThrow(
                        () -> new TokenNotFoundException("Invalid verification token")
                );
        BaseUser user = baseUserRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new UserNotFoundException("There is no user with given Id"));

        if(!token.getUser().equals(user)){
            throw new AccessDeniedException("It is not your token");
        }
        passwordService.changePassword(user, request.getPassword());

        token.setStatus(ForgotPasswordVerificationToken.ForgotPasswordTokenStatus.CONFIRMATION_TOKEN_USED);
        forgotPasswordVerificationTokenRepository.save(token);
        return new ForgotPasswordResponse("");
    }

}
