package com.example.carrentingapp.email.notifications.forgot_password;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ForgotPasswordRequest implements NotificationRequestInterface {
    private UserBase user;
}
