package com.example.carrentingapp.user.service;

import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserPasswordService {

    private final PasswordEncoder passwordEncoder;
    private final UserBaseRepository baseUserRepository;

    public void changePassword(UserBase user, String oldPassword, String newPassword){
        if(passwordEncoder.matches(oldPassword, user.getPassword())){
            user.setPassword(passwordEncoder.encode(newPassword));
        }
        baseUserRepository.save(user);
    }

    public void changePassword(UserBase user, String newPassword){
        user.setPassword(passwordEncoder.encode(newPassword));
        baseUserRepository.save(user);
    }
}
