package com.example.carrentingapp.authentication.controller;

import com.example.carrentingapp.authentication.request.ForgotPasswordVerificationRequest;
import com.example.carrentingapp.authentication.request.NewPasswordRequest;
import com.example.carrentingapp.authentication.response.ForgotPasswordResponse;
import com.example.carrentingapp.authentication.service.ForgotPasswordService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth/forgot-password")
public class ForgotPasswordController {

    private final ForgotPasswordService forgotPasswordService;

    @PostMapping("/email")
    public ResponseEntity<ForgotPasswordResponse> sendVerificationToken(
            @RequestBody ForgotPasswordVerificationRequest request
    ){
        return ResponseEntity.ok(forgotPasswordService.sendEmail(request));
    }

    @PostMapping("/set-new")
    public ResponseEntity<ForgotPasswordResponse> changePassword(
            @RequestBody NewPasswordRequest request
    ){
        return ResponseEntity.ok(forgotPasswordService.changePassword(request));
    }

}

