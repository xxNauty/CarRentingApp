package com.example.carrentingapp.rent.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CollectCarRequest {
    private UUID carRentId;
}
