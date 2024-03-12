package com.example.carrentingapp.unit.user;

import com.example.carrentingapp.exception.exception.http_error_403.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.service.UserCreateService;
import com.example.carrentingapp.user.service.UserLockService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.Objects;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserLockServiceTest {

    @Autowired
    private UserLockService service;

    @Autowired
    private BaseUserRepository repository;

    @Autowired
    private UserCreateService createService;

    @Autowired
    private UserLockRepository lockRepository;

    @Test
    public void testUserLock(){
        BaseUser user = createService.createUser(
                "Adam",
                "Kowalski",
                "adam123321@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );
        Assertions.assertFalse(user.getIsLocked());

        service.lockUser(new LockRequest(
                user.getId(),
                UserLock.LockType.TEMPORARY.name(),
                UserLock.Reason.DAMAGED_CAR.name(),
                LocalDate.now()
        ));

        BaseUser userFromDatabase = repository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertTrue(userFromDatabase.getIsLocked());

        lockRepository.findAllActiveLockForUser(user.getId()).orElseThrow(() -> new UserNotLockedException("User not locked"));
        Assertions.assertDoesNotThrow(() -> new UserNotLockedException("User not locked"));
    }

    @Test
    public void testUserUnlock(){
        BaseUser user = createService.createUser(
                "Adam",
                "Kowalski",
                "adam1221@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );
        Assertions.assertFalse(user.getIsLocked());

        service.lockUser(new LockRequest(
                user.getId(),
                UserLock.LockType.TEMPORARY.name(),
                UserLock.Reason.DAMAGED_CAR.name(),
                LocalDate.now()
        ));

        BaseUser userAfterLock = repository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertTrue(userAfterLock.getIsLocked());

        service.unlockUser(new UnlockRequest(user.getId()));

        BaseUser userAfterUnlock = repository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertFalse(userAfterUnlock.getIsLocked());

        UserLock lock = lockRepository.findAllActiveLockForUser(user.getId()).orElse(new UserLock());
        Assertions.assertEquals(lock, new UserLock());
    }

}
