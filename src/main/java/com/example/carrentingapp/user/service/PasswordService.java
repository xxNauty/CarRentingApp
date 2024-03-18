package com.example.carrentingapp.user.service;

import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PasswordService {

    private final PasswordEncoder passwordEncoder;
    private final BaseUserRepository baseUserRepository;

    public void changePassword(BaseUser user, String oldPassword, String newPassword){
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        baseUserRepository.save(user);
    }

    public void changePassword(BaseUser user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        baseUserRepository.save(user);
    }
}
