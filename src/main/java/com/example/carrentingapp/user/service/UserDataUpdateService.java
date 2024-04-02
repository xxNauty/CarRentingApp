package com.example.carrentingapp.user.service;

import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailRequest;
import com.example.carrentingapp.exception.exception.http_error_403.AccessDeniedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.request.UserDataUpdateRequest;
import com.example.carrentingapp.user.response.UserDataUpdateResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserDataUpdateService {

    private final UserBaseRepository userBaseRepository;
    private final SecurityService securityService;
    private final EmailNotificationSender emailNotificationSender;

    @Transactional
    public UserDataUpdateResponse updateData(UserDataUpdateRequest request) {
        if (!securityService.getLoggedInUser().getId().equals(UUID.fromString(request.userId.get())) &&
                !securityService.getLoggedInUser().getRole().equals(UserBase.Role.ADMIN)) {
            throw new AccessDeniedException("You cannot modify data of this user");
        }

        UserBase user = userBaseRepository.findById(
                UUID.fromString(request.userId.get())
        ).orElseThrow(() -> new UserNotFoundException("There is no user with given id"));

        user.setFirstName(request.firstName.get());
        user.setLastName(request.lastName.get());
        user.setEmail(request.email.get());
        user.setDateOfBirth(request.dateOfBirth.get());
        user.setStatus(UserBase.UserStatus.USER_CREATED);

        userBaseRepository.save(user);
        emailNotificationSender.sendConfirmEmailNotification(new ConfirmEmailRequest(user));

        return new UserDataUpdateResponse("User data updated successfully");
    }

}
