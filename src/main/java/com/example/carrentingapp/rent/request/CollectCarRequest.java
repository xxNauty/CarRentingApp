package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CollectCarRequest {
    private UUID carRentId;
}
