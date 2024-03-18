package com.example.carrentingapp.user.service;

import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedRequest;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.UnlockRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserUnlockService {

    private final UserLockRepository userLockRepository;
    private final UserLockService userLockService;

    public void check(){
        List<UserLock> locks = userLockRepository.findAllByIsActive(true);
        for (UserLock lock : locks){
            if (lock.getExpirationDate().equals(LocalDate.now())){
                userLockService.unlockUser(new UnlockRequest(lock.getUser().getId()));
            }
        }
    }

}
