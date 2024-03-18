package com.example.carrentingapp.car.request;

import com.example.carrentingapp.car.CarLock;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class CarLockRequest {
    private UUID carId;
    private String reason;
    private LocalDate lockedTo;
}
