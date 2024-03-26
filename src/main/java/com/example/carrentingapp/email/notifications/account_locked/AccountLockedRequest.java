package com.example.carrentingapp.email.notifications.account_locked;

import com.example.carrentingapp.email.notifications.NotificationRequest;
import com.example.carrentingapp.user.UserLock;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountLockedRequest implements NotificationRequest {
    private UserLock lock;
}
