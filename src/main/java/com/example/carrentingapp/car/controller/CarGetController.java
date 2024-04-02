package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.response.CarGetFullListResponse;
import com.example.carrentingapp.car.response.CarGetResponse;
import com.example.carrentingapp.car.response.CarGetSimpleListResponse;
import com.example.carrentingapp.car.service.CarGetService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car/get")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CarGetController {

    private final CarGetService carGetService;

    @GetMapping
    public ResponseEntity<CarGetResponse> getCarById(
            @RequestParam UUID id
    ) {
        return ResponseEntity.ok(carGetService.getCarById(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/simple/all")
    public ResponseEntity<CarGetSimpleListResponse> getSimpleCarList() {
        return ResponseEntity.ok(carGetService.getSimpleCarList(false));
    }

    @GetMapping("/simple/available")
    public ResponseEntity<CarGetSimpleListResponse> getSimpleAvailableCarList() {
        return ResponseEntity.ok(carGetService.getSimpleCarList(true));
    }

    @GetMapping("/full/all")
    public ResponseEntity<CarGetFullListResponse> getFullCarList() {
        return ResponseEntity.ok(carGetService.getFullCarList(false));
    }

    @GetMapping("full/available")
    public ResponseEntity<CarGetFullListResponse> getFullAvailableCarList() {
        return ResponseEntity.ok(carGetService.getFullCarList(true));
    }
}
