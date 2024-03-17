package com.example.carrentingapp.user.service;

import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.account_locked.AccountLockedRequest;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedRequest;
import com.example.carrentingapp.exception.exception.http_error_403.AccountAlreadyLockedException;
import com.example.carrentingapp.exception.exception.http_error_403.AccountUnlockImpossibleException;
import com.example.carrentingapp.exception.exception.http_error_403.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserLockService {

    private final UserLockRepository repository;
    private final BaseUserRepository userRepository;
    private final NotificationSender notificationSender;

    public LockResponse lockUser(LockRequest request) {
        BaseUser user = userRepository.findById(request.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(user.getIsLocked()){
            throw new AccountAlreadyLockedException("You can not lock already locked account");
        }

        UserLock lock = new UserLock(
                UserLock.Reason.valueOf(request.getReason()),
                UserLock.LockType.valueOf(request.getLockType()),
                request.getLockType().equals(UserLock.LockType.TEMPORARY.name()) ? request.getExpirationDate() : null,
                user
        );

        user.setIsLocked(true);

        userRepository.save(user);
        repository.save(lock);

        notificationSender.sendAccountLockedNotification(new AccountLockedRequest(lock));

        return new LockResponse("User account is now locked");
    }

    public LockResponse unlockUser(UnlockRequest request) {
        BaseUser user = userRepository.findById(request.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(!user.getIsLocked()){
            throw new UserNotLockedException("This user is not locked");
        }

        UserLock lock = repository.findAllActiveLockForUser(request.getUserid())
                .orElseThrow(() -> new UserLockNotFoundException("There is no active locks for this user"));

        if(lock.getType().equals(UserLock.LockType.FOREVER)){
            throw new AccountUnlockImpossibleException("This account is locked forever");
        }

        user.setIsLocked(false);
        lock.setIsActive(false);

        userRepository.save(user);
        repository.save(lock);

        notificationSender.sendAccountUnlockedNotification(new AccountUnlockedRequest(user));

        return new LockResponse("User account is now unlocked");
    }

}
