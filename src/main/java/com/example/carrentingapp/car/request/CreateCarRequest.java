package com.example.carrentingapp.car.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateCarRequest {
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private Float mileage;
    private Float power;
    private Float torque;
    private Float engineSize;
    private Float averageFuelConsumption;
    private Float minRankOfUser;
    private Float pricePerDay;
}
