package com.example.carrentingapp.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ForgotPasswordVerificationRequest {
    private String email;
}
