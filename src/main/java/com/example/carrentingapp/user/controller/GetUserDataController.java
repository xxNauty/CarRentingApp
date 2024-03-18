package com.example.carrentingapp.user.controller;

import com.example.carrentingapp.user.response.GetUserDataResponse;
import com.example.carrentingapp.user.service.GetUserDataService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    private final GetUserDataService gerUserDataService;

    @GetMapping("/get")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<GetUserDataResponse> getUserDataAsUser(){
        return ResponseEntity.ok(gerUserDataService.getUserData());
    }

    @GetMapping("/get/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GetUserDataResponse> getUserDataAsAdmin(
            @RequestParam String id
    ){
        return ResponseEntity.ok(gerUserDataService.getUserData(id));
    }

}
