package com.example.carrentingapp.car.request;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@AllArgsConstructor
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
