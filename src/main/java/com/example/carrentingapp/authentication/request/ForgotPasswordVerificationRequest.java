package com.example.carrentingapp.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordVerificationRequest {
    private String email;
}
