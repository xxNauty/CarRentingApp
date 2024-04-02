package com.example.carrentingapp.configuration.service;

import com.example.carrentingapp.exception.exception.http_error_403.AccessDeniedException;
import com.example.carrentingapp.user.UserBase;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

    public UserBase getLoggedInUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.toString().equals("anonymousUser")) {
            throw new AccessDeniedException("You cannot do this not logged");
        }
        return (UserBase) principal;
    }

}
