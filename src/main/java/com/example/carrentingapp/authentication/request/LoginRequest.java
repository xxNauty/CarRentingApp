package com.example.carrentingapp.authentication.request;

import lombok.*;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;

}
