package com.example.carrentingapp.user.response;

import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateResponse {
    private String message;
    private UserBase userBase;
}
