package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarReturnRequest {
    private UUID carRentId;
    private Float kilometersTraveled;

}
