package com.example.carrentingapp.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewPasswordRequest {
    private String email;
    private String token;
    private String password;
}
