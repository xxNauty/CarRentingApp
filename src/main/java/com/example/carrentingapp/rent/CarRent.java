package com.example.carrentingapp.rent;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.user.UserBase;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
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
    private CarBase car;

    @JsonIgnore
    @ManyToOne
    private UserBase user;

    private LocalDate rentedFrom;

    private LocalDate rentedTo;

    @Enumerated(EnumType.STRING)
    private CarRentStatus status;

    private Integer daysOfDelay;

    public CarRent(CarBase car, UserBase user, LocalDate rentedFrom, LocalDate rentedTo) {
        this.car = car;
        this.user = user;
        this.rentedFrom = rentedFrom;
        this.rentedTo = rentedTo;
        this.status = CarRentStatus.CAR_RENT_CREATED;
        this.daysOfDelay = 0;
    }

    public enum CarRentStatus{
        CAR_RENT_CREATED,
        CAR_RENT_CAR_READY_TO_COLLECT,
        CAR_RENT_CAR_COLLECTED,
        CAR_RENT_WAITING_FOR_REVIEW,
        CAR_RENT_END_OF_RENT_OK,
        CAR_RENT_END_OF_RENT_DAMAGED_CAR,
        CAR_RENT_END_OF_RENT_LATE;

        public boolean isIn(final CarRentStatus... statuses){
            return Arrays.asList(statuses).contains(this);
        }
    }


}
