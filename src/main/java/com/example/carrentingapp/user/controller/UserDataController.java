package com.example.carrentingapp.user.controller;

import com.example.carrentingapp.user.request.UserDataUpdateRequest;
import com.example.carrentingapp.user.response.UserDataUpdateResponse;
import com.example.carrentingapp.user.service.UserDataUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/update")
@AllArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserDataController {

    private final UserDataUpdateService userDataUpdateService;

    @PostMapping
    public ResponseEntity<UserDataUpdateResponse> updateUserData(
            @RequestBody UserDataUpdateRequest request
    ) {
        return ResponseEntity.ok(userDataUpdateService.updateData(request));
    }
}
