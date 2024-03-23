package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarCheckAfterRentRequest {
    private UUID carRentId;
    private boolean isDamaged;
}
