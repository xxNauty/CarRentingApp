package com.example.carrentingapp.car.request;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarUpdateDataRequest {
    private UUID id;
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private Float power;
    private Float torque;
    private Float engineSize;
    private Float averageFuelConsumption;
    private Float minRankOfUser;
    private Float pricePerDay;
}
