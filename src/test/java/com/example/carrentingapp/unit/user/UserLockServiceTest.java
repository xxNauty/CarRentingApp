package com.example.carrentingapp.unit.user;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.exception.exception.http_error_403.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.service.UserLockService;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserLockServiceTest {

    @Autowired
    private UserLockService userLockService;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Test
    public void testUserLock(){
        BaseUser user = commonFunctionsProvider.createUser();
        Assertions.assertFalse(user.getIsLocked());

        userLockService.lockUser(new LockRequest(
                user.getId(),
                UserLock.LockType.TEMPORARY.name(),
                UserLock.Reason.DAMAGED_CAR.name(),
                LocalDate.now()
        ));

        BaseUser userFromDatabase = baseUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertTrue(userFromDatabase.getIsLocked());

        userLockRepository.findAllActiveLockForUser(user.getId()).orElseThrow(() -> new UserNotLockedException("User not locked"));
        Assertions.assertDoesNotThrow(() -> new UserNotLockedException("User not locked"));
    }

    @Test
    public void testUserUnlock(){
        BaseUser user = commonFunctionsProvider.createUser();
        Assertions.assertFalse(user.getIsLocked());

        userLockService.lockUser(new LockRequest(
                user.getId(),
                UserLock.LockType.TEMPORARY.name(),
                UserLock.Reason.DAMAGED_CAR.name(),
                LocalDate.now()
        ));

        BaseUser userAfterLock = baseUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertTrue(userAfterLock.getIsLocked());

        userLockService.unlockUser(new UnlockRequest(user.getId()));

        BaseUser userAfterUnlock = baseUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertFalse(userAfterUnlock.getIsLocked());

        UserLock lock = userLockRepository.findAllActiveLockForUser(user.getId()).orElse(new UserLock());
        Assertions.assertEquals(lock, new UserLock());
    }

}
