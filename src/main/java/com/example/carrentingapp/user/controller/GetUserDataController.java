package com.example.carrentingapp.user.controller;

import com.example.carrentingapp.user.response.UserGetDataResponse;
import com.example.carrentingapp.user.service.UserGetDataService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/user")
public class GetUserDataController {

    private final UserGetDataService gerUserDataService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserGetDataResponse> getUserDataAsUser(){
        return ResponseEntity.ok(gerUserDataService.getUserData());
    }

    @GetMapping("/get/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserGetDataResponse> getUserDataAsAdmin(
            @RequestParam String id
    ){
        return ResponseEntity.ok(gerUserDataService.getUserData(id));
    }

}
