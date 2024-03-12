package com.example.carrentingapp.car;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BaseCar {

    @Id
    @GeneratedValue
    private UUID id;
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private Float mileage;
    private Boolean isRented;
    private Float power;
    private Float torque;
    private Float engineSize;
    private Float averageFuelConsumption;
    private Float minRankOfUser;
    private Float pricePerDay;
    private Boolean hasActiveSale;
    private Boolean isAvailable;
}
