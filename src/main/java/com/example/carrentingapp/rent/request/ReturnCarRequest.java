package com.example.carrentingapp.rent.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ReturnCarRequest {
    private UUID carRentId;
    private Float kilometersTraveled;

}
