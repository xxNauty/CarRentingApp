package com.example.carrentingapp.rent.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarRentResponse {
    private String message;
    private Boolean isActive;
    private UUID carRentId;
}
