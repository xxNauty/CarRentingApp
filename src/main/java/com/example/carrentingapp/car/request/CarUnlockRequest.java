package com.example.carrentingapp.car.request;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarUnlockRequest {
    private UUID carId;
}
