package com.example.carrentingapp.email.notifications.account_locked;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.UserLock;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountLockedRequest implements NotificationRequestInterface {
    private UserLock lock;
}
