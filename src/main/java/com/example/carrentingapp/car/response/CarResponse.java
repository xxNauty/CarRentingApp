package com.example.carrentingapp.car.response;

import lombok.*;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarResponse {
    private UUID id;

    private String message;
}
