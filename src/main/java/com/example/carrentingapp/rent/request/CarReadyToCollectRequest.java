package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.user.UserBase;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarReadyToCollectRequest {
    private UserBase user;
}
