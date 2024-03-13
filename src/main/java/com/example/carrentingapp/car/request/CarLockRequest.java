package com.example.carrentingapp.car.request;

import com.example.carrentingapp.car.CarLock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarLockRequest {
    private UUID carId;
    private String reason;
    private LocalDate lockedTo;
}
