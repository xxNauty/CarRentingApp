package com.example.carrentingapp.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    USER("user"),

    ADMIN("admin");

    private final String role;
}
