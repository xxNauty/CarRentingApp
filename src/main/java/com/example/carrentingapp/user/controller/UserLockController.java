package com.example.carrentingapp.user.controller;

import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import com.example.carrentingapp.user.service.UserLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserLockController {

    private final UserLockService service;

    @PostMapping("/lock")
    public ResponseEntity<LockResponse> lockUser(
            @RequestBody LockRequest request
    ){
        return ResponseEntity.ok(service.lockUser(request));
    }

    @PostMapping("/unlock")
    public ResponseEntity<LockResponse> unlockUser(
            @RequestBody UnlockRequest request
    ){
        return ResponseEntity.ok(service.unlockUser(request));
    }
}
