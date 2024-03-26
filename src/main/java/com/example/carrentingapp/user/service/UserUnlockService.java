package com.example.carrentingapp.user.service;

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
public class UserUnlockService {

    private final UserLockRepository userLockRepository;
    private final UserLockService userLockService;

    public void check(){
        List<UserLock> locks = userLockRepository.findAllByStatus(UserLock.UserLockStatus.USER_LOCK_ACTIVE);
        for (UserLock lock : locks){
            if (lock.getExpirationDate().equals(LocalDate.now())){
                userLockService.unlockUser(new UnlockRequest(Optional.of(lock.getUser().getId().toString())));
            }
        }
    }

}
