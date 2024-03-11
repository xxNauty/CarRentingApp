package com.example.carrentingapp.user.service;

import com.example.carrentingapp.exception.exception.http_error_403.AccountAlreadyLockedException;
import com.example.carrentingapp.exception.exception.http_error_403.AccountUnlockImpossibleException;
import com.example.carrentingapp.exception.exception.http_error_403.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserLockNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.*;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserLockService {

    private final UserLockRepository repository;
    private final BaseUserRepository userRepository;

    public LockResponse lockUser(LockRequest request) {
        BaseUser user = userRepository.findById(request.getUserid())
                .orElseThrow(() -> new UserNotFoundException("User with given ID not found"));

        if(user.getIsLocked()){
            throw new AccountAlreadyLockedException("You can not lock already locked account");
        }

        UserLock lock = new UserLock(
                Reason.valueOf(request.getReason()),
                LockType.valueOf(request.getLockType()),
                request.getLockType().equals(LockType.TEMPORARY.name()) ? request.getExpirationDate() : null,
                user
        );

        user.setIsLocked(true);

        userRepository.save(user);
        repository.save(lock);

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

        if(lock.getType().equals(LockType.FOREVER)){
            throw new AccountUnlockImpossibleException("This account is locked forever");
        }

        user.setIsLocked(false);
        lock.setIsActive(false);

        userRepository.save(user);
        repository.save(lock);

        return new LockResponse("User account is now unlocked");
    }

}
