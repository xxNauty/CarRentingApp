package com.example.carrentingapp.rent;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.user.BaseUser;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "car_rent")
@NoArgsConstructor
public class CarRent {
    @Id
    @GeneratedValue
    private UUID id;
    @ManyToOne
    private BaseCar car;
    @ManyToOne
    private BaseUser user;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;
    private LocalDateTime collectionDate;
    private Boolean isActive;
    private Boolean collectedCar;

    public CarRent(BaseCar car, BaseUser user, LocalDate rentedFrom, LocalDate rentedTo, LocalDateTime collectionDate, Boolean isActive) {
        this.car = car;
        this.user = user;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.collectionDate = collectionDate;
        this.isActive = isActive;
        this.collectedCar = false;
    }
}
