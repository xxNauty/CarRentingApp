package com.example.carrentingapp.car.request;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UpdateCarMileageRequest {
    private UUID carId;
    private Float mileageToAdd;
}
