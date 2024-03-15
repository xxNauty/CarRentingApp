package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class CarRentRequest {
    private String carId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;

}
