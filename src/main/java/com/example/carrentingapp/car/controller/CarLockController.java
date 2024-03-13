package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.car.service.CarLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/car")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class CarLockController {

    private final CarLockService carLockService;

    @PostMapping("/lock")
    public ResponseEntity<CarLockResponse> lockCar(
            @RequestBody CarLockRequest request
    ){
        return ResponseEntity.ok(carLockService.lockCar(request));
    }

    @PostMapping("/unlock")
    public ResponseEntity<CarLockResponse> unlockCar(
            @RequestBody CarUnlockRequest request
    ){
        return ResponseEntity.ok(carLockService.unlockCar(request));
    }

}
