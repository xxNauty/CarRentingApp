package com.example.carrentingapp.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {

    ADMIN_CREATE_CAR("admin:create_car"),
    ADMIN_UPDATE_CAR("admin:update_car"),
    USER_GET_CAR("user:get_car");

    private final String permission;
}
