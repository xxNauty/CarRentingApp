package com.example.carrentingapp.email.notifications.confirm_email;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmEmailRequest implements NotificationRequestInterface {
    private UserBase user;
}
