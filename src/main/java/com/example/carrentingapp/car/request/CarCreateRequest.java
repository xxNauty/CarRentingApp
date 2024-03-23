package com.example.carrentingapp.car.request;

import lombok.*;

@Getter
@AllArgsConstructor
public class CarCreateRequest {
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
