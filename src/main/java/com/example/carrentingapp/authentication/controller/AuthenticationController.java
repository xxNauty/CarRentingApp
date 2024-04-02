package com.example.carrentingapp.authentication.controller;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.request.SendVerifyingTokenAgainRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.EmailVerificationResponse;
import com.example.carrentingapp.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegistrationRequest request
    ) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(authenticationService.login(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request, response);
    }

    @GetMapping("/register/verify")
    public ResponseEntity<EmailVerificationResponse> verifyEmail(
            @RequestParam String token
    ) {
        return ResponseEntity.ok(authenticationService.verifyEmail(token));
    }

    @PostMapping("/register/verify/send-again")
    public ResponseEntity<EmailVerificationResponse> sendVerifyingTokenAgain(
            @RequestBody SendVerifyingTokenAgainRequest request
    ) {
        return ResponseEntity.ok(authenticationService.sendVerifyingTokenAgain(request));
    }
}
