package com.example.carrentingapp.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserPasswordService {

    private final PasswordEncoder passwordEncoder;

    public String encodePassword(String plainPassword){
        return passwordEncoder.encode(plainPassword);
    }

    public Boolean passwordMatches(String plainPassword, BaseUser user){
        return user.getPassword().equals(passwordEncoder.encode(plainPassword));
    }

}
