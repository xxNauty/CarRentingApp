package com.example.carrentingapp.email.notifications.confirm_email;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ConfirmEmailRequest implements NotificationRequest {
    private UserBase user;
}
