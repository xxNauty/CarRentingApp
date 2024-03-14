package com.example.carrentingapp.rent.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CollectCarRequest {
    private UUID carRentId;
}
