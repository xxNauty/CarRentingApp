package com.example.carrentingapp.email.notifications.account_unlocked;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUnlockedRequest implements NotificationRequest {
    private UserBase user;
}
