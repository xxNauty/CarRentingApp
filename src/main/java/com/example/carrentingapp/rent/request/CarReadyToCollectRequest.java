package com.example.carrentingapp.rent.request;

import com.example.carrentingapp.user.BaseUser;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarReadyToCollectRequest {
    private BaseUser user;
}
