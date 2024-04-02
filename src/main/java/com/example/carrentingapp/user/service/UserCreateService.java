package com.example.carrentingapp.user.service;

import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailRequest;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.response.UserCreateResponse;
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

    @Transactional
    public UserCreateResponse createUser(RegistrationRequest request) {
        UserBase user = new UserBase(
                request.firstName.get(),
                request.lastName.get(),
                request.email.get(),
                passwordEncoder.encode(request.password.get()),
                request.dateOfBirth.get()
        );

        notificationSender.sendConfirmEmailNotification(new ConfirmEmailRequest(user));

        userRepository.save(user);

        return new UserCreateResponse("User created successfully", user);
    }

    public UserBase createAdmin(
            String firstName,
            String lastName,
            String email,
            String password,
            LocalDate dateOfBirth
    ) {
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
