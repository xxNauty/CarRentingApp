package com.example.carrentingapp.user.service;

import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.response.UserGetDataResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class UserGetDataService {

    private final SecurityService securityService;
    private final UserBaseRepository baseUserRepository;

    public UserGetDataResponse getUserData(){
        return new UserGetDataResponse(securityService.getLoggedInUser());
    }

    public UserGetDataResponse getUserData(String id){
        UserBase user = baseUserRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UserNotFoundException("There is no user with given id"));
        return new UserGetDataResponse(user);
    }
}
