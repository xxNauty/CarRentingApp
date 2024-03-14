package com.example.carrentingapp.rent.request;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class CarRentRequest {
    private String carId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;

}
