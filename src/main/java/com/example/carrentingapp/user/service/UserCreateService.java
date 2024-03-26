package com.example.carrentingapp.user.service;

import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailRequest;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class UserCreateService {

    private final UserBaseRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailNotificationSender notificationSender;

    //todo: przerobić na UserCreateResponse
    @Transactional
    public UserBase createUser(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        UserBase user = new UserBase(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                dateOfBirth
        );

        notificationSender.sendConfirmEmailNotification(new ConfirmEmailRequest(user));

        userRepository.save(user);

        return user;
    }

    public UserBase createAdmin(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ){
        UserBase user = new UserBase(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password),
                dateOfBirth
        );
        user.setRole(UserBase.Role.ADMIN);
        user.setStatus(UserBase.UserStatus.USER_READY); //admin nie musi potwierdzać adresu email

        userRepository.save(user);

        return user;
    }

}
