package com.example.carrentingapp.authentication.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NewPasswordRequest {
    private String email;
    private String token;
    private String password;
}
