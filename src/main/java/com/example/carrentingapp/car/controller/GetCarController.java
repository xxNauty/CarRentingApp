package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.response.GetCarListResponse;
import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.service.CarGetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/car/get")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class GetCarController {

    private final CarGetService service;

    @GetMapping("/{id}")
    public ResponseEntity<GetCarResponse> getCarById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getCarById(id));
    }

    @GetMapping("/all")
    public ResponseEntity<GetCarListResponse> getAllCars(){
        return ResponseEntity.ok(service.getAllCars());
    }
}
