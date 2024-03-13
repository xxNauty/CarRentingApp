package com.example.carrentingapp.user.service;

import com.example.carrentingapp.email_verification.notifications.confirm_email.ConfirmEmailNotificationSender;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.enums.Role;
import com.example.carrentingapp.user.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserCreateService {

    private final BaseUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ConfirmEmailNotificationSender confirmEmailNotificationSender;

    public BaseUser createUser(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        BaseUser user = new BaseUser(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                dateOfBirth
        );

        confirmEmailNotificationSender.sendConfirmationEmail(user);

        userRepository.save(user);

        return user;
    }

    public BaseUser createAdmin(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        BaseUser user = new BaseUser(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                dateOfBirth
        );
        user.setRole(Role.ADMIN);
        user.setIsEnabled(true); //admin nie musi potwierdzać adresu email

        userRepository.save(user);

        return user;
    }

}
