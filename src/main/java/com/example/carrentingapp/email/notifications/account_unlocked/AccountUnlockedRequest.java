package com.example.carrentingapp.email.notifications.account_unlocked;

import com.example.carrentingapp.email.notifications.NotificationRequestInterface;
import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccountUnlockedRequest implements NotificationRequestInterface {
    private BaseUser user;
}
