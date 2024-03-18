package com.example.carrentingapp.car.request;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarUnlockRequest {
    private UUID carId;
}
