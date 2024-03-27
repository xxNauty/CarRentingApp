package com.example.carrentingapp.user.service;

import com.example.carrentingapp.configuration.common_interfaces.ScheduledAction;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.UnlockRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserUnlockService implements ScheduledAction {

    private final UserLockRepository userLockRepository;
    private final UserBaseRepository userBaseRepository;
    private final UserLockService userLockService;

    @Override
    public void check(){
        List<UserLock> locks = userLockRepository.findAllByStatusAndType(UserLock.UserLockStatus.USER_LOCK_ACTIVE, UserLock.LockType.TEMPORARY);
        for (UserLock lock : locks){
            if (lock.getExpirationDate().equals(LocalDate.now())){
                userLockService.unlockUser(new UnlockRequest(Optional.of(lock.getUser().getId().toString())));
                if(lock.getReason().equals(UserLock.Reason.FREQUENT_DELAYED_RETURNS)){
                    UserBase user = lock.getUser();
                    user.setCarReturnDelays(0);
                    userBaseRepository.save(user);
                }
            }
        }
    }

}
