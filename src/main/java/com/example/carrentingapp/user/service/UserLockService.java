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

@Service
@Transactional
@AllArgsConstructor
public class UserLockService {

    private final UserLockRepository repository;
    private final UserBaseRepository userRepository;
    private final NotificationSender notificationSender;

    public LockResponse lockUser(LockRequest request) {
        UserBase user = userRepository.findById(request.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(!user.isAccountNonLocked()){
            throw new AccountAlreadyLockedException("You can not lock already locked account");
        }

        UserLock lock = new UserLock(
                UserLock.Reason.valueOf(request.getReason()),
                UserLock.LockType.valueOf(request.getLockType()),
                request.getLockType().equals(UserLock.LockType.TEMPORARY.name()) ? request.getExpirationDate() : null,
                user
        );

        if (request.getLockType().equals(UserLock.LockType.TEMPORARY.name())){
            user.setStatus(UserBase.UserStatus.USER_LOCKED_TEMPORARY);
        }
        else if (request.getLockType().equals(UserLock.LockType.FOREVER.name())){
            user.setStatus(UserBase.UserStatus.USER_LOCKED_FOREVER);
        }

        userRepository.save(user);
        repository.save(lock);

        notificationSender.sendAccountLockedNotification(new AccountLockedRequest(lock));

        return new LockResponse("User account is now locked");
    }

    public LockResponse unlockUser(UnlockRequest request) {
        UserBase user = userRepository.findById(request.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(user.isAccountNonLocked()){
            throw new UserNotLockedException("This user is not locked");
        }

        UserLock lock = repository.findAllByStatusAndUser(request.getUserid(), UserLock.UserLockStatus.USER_LOCK_ACTIVE)
                .orElseThrow(() -> new UserLockNotFoundException("There is no active locks for this user"));

        if(lock.getType().equals(UserLock.LockType.FOREVER)){
            throw new AccountUnlockImpossibleException("This account is locked forever");
        }

        user.setStatus(UserBase.UserStatus.USER_READY);
        lock.setStatus(UserLock.UserLockStatus.USER_LOCK_NOT_ACTIVE);

        userRepository.save(user);
        repository.save(lock);

        notificationSender.sendAccountUnlockedNotification(new AccountUnlockedRequest(user));

        return new LockResponse("User account is now unlocked");
    }

}
