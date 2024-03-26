package com.example.carrentingapp.user.service;

import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.account_locked.AccountLockedRequest;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedRequest;
import com.example.carrentingapp.exception.exception.http_error_500.AccountAlreadyLockedException;
import com.example.carrentingapp.exception.exception.http_error_500.AccountUnlockImpossibleException;
import com.example.carrentingapp.exception.exception.http_error_500.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@AllArgsConstructor
public class UserLockService {

    private final UserLockRepository userLockRepository;
    private final UserBaseRepository userBaseRepository;
    private final NotificationSender notificationSender;

    public LockResponse lockUser(LockRequest request) {
        request.checkInput();
        UserBase user = userBaseRepository.findById(UUID.fromString(request.userid.get()))
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(!user.isAccountNonLocked()){
            UserLock lock = userLockRepository.findActiveForUser(
                    UUID.fromString(request.userid.get())
            ).orElseThrow(() -> new UserLockNotFoundException("User lock not found"));
            lock.setStatus(UserLock.UserLockStatus.USER_LOCK_EXTENDED);
            userLockRepository.save(lock);
        }

        UserLock lock = new UserLock(
                UserLock.Reason.valueOf(request.reason.get()),
                UserLock.LockType.valueOf(request.lockType.get()),
                request.lockType.get().equals(UserLock.LockType.TEMPORARY.name()) ? request.expirationDate.get() : null,
                user
        );

        if (request.lockType.get().equals(UserLock.LockType.TEMPORARY.name())){
            user.setStatus(UserBase.UserStatus.USER_LOCKED_TEMPORARY);
        }
        else if (request.lockType.get().equals(UserLock.LockType.FOREVER.name())){
            user.setStatus(UserBase.UserStatus.USER_LOCKED_FOREVER);
        }

        userBaseRepository.save(user);
        userLockRepository.save(lock);

        notificationSender.sendAccountLockedNotification(new AccountLockedRequest(lock));

        return new LockResponse("User account is now locked");
    }

    public LockResponse unlockUser(UnlockRequest request) {
        request.checkInput();
        UserBase user = userBaseRepository.findById(UUID.fromString(request.userid.get()))
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(user.isAccountNonLocked()){
            throw new UserNotLockedException("This user is not locked");
        }

        UserLock lock = userLockRepository.findActiveForUser(UUID.fromString(request.userid.get()))
                .orElseThrow(() -> new UserLockNotFoundException("There is no active locks for this user"));

        if(lock.getType().equals(UserLock.LockType.FOREVER)){
            throw new AccountUnlockImpossibleException("This account is locked forever");
        }

        user.setStatus(UserBase.UserStatus.USER_READY);
        lock.setStatus(UserLock.UserLockStatus.USER_LOCK_NOT_ACTIVE);

        userBaseRepository.save(user);
        userLockRepository.save(lock);

        notificationSender.sendAccountUnlockedNotification(new AccountUnlockedRequest(user));

        return new LockResponse("User account is now unlocked");
    }

}
