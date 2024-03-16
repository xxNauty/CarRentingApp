package com.example.carrentingapp.user.service;

import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.response.GetUserDataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetUserDataService {

    private final SecurityService securityService;
    private final BaseUserRepository baseUserRepository;

    public GetUserDataResponse getUserData(){
        return new GetUserDataResponse(securityService.getLoggedInUser());
    }

    public GetUserDataResponse getUserData(String id){
        BaseUser user = baseUserRepository.findById(UUID.fromString(id)).orElseThrow(() -> new UserNotFoundException("There is no user with given id"));
        return new GetUserDataResponse(user);
    }
}
