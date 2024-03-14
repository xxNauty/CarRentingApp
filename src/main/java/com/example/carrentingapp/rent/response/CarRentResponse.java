package com.example.carrentingapp.rent.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class CarRentResponse {
    private String message;
    private Boolean isActive;
    private UUID carRentId;
}
