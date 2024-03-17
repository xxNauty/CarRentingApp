package com.example.carrentingapp.email.notifications.account_locked;

import com.example.carrentingapp.user.UserLock;

public class AccountLockedTemplate {
    public static String template(UserLock lock){
        String lockExpiration = "";
        if(lock.getType().equals(UserLock.LockType.TEMPORARY)){
            lockExpiration = "It will be unlocked at " + lock.getExpirationDate().toString();
        }
        return
                "<h1>Hello!</h1>" +
                        "<h2>Your account has been locked. Reason: " +
                        lock.getReason().name().toLowerCase() +
                        ". " +
                        lockExpiration +
                        "</h2>";
    }
}
