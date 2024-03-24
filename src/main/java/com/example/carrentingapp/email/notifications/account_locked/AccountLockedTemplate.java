package com.example.carrentingapp.email.notifications.account_locked;

import com.example.carrentingapp.user.UserLock;

public class AccountLockedTemplate {
    public static String template(UserLock lock){
        return
               lock.getType().equals(UserLock.LockType.FOREVER)
                       ? "<h1>Hello</h1><h2>Your account has been locked forever. Reason: " + lock.getReason().name()
                       : "<h1>Hello</h1><h2>Your account has been locked. Reason: " + lock.getReason().name() +
                        "You will be unlocked at: " + lock.getExpirationDate();
    }
}
