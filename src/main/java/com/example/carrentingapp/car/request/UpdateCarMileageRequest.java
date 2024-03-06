package com.example.carrentingapp.car.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCarMileageRequest {
    private UUID carId;
    private Float mileageToAdd;
}
