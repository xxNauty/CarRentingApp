package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Optional;

@Getter
@AllArgsConstructor
public class CarRentRequest {


//    public Optional<String> carId;
//    public Optional<LocalDate> rentedFrom;
//    public Optional<LocalDate> rentedTo;
//
//    public void checkInput() {
//        // NOP
//    }

    private String carId;
    private LocalDate rentedFrom;
    private LocalDate rentedTo;

}
