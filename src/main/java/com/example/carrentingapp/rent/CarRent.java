package com.example.carrentingapp.rent;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.user.BaseUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "car_rent")
public class CarRent {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private BaseCar car;

    @JsonIgnore
    @ManyToOne
    private BaseUser user;

    private LocalDate rentedFrom;

    private LocalDate rentedTo;

    private LocalDateTime collectionDate;

    @Enumerated(EnumType.STRING)
    private CarRentStatus status;

    public CarRent(BaseCar car, BaseUser user, LocalDate rentedFrom, LocalDate rentedTo, LocalDateTime collectionDate) {
        this.car = car;
        this.user = user;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.collectionDate = collectionDate;
        this.status = CarRentStatus.CAR_RENT_CREATED;
    }

    public enum CarRentStatus{
        CAR_RENT_CREATED,
        CAR_RENT_CAR_WAITING,
        CAR_RENT_CAR_READY_TO_COLLECT,
        CAR_RENT_CAR_COLLECTED,
        CAR_RENT_WAITING_FOR_REVIEW,
        CAR_RENT_END_OF_RENT_OK,
        CAR_RENT_END_OF_RENT_DAMAGED_CAR,
        CAR_RENT_END_OF_RENT_LATE
    }
}
