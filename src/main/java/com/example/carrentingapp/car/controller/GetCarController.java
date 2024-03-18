package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.service.CarGetService;
import com.example.carrentingapp.car.response.GetFullCarListResponse;
import com.example.carrentingapp.car.response.GetSimpleCarListResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car/get")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class GetCarController {

    private final CarGetService service;

    @GetMapping("/{id}")
    public ResponseEntity<GetCarResponse> getCarById(@PathVariable UUID id){
        return ResponseEntity.ok(service.getCarById(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/simple/all")
    public ResponseEntity<GetSimpleCarListResponse> getSimpleCarList(){
        return ResponseEntity.ok(service.getSimpleCarList(false));
    }

    @GetMapping("/simple/available")
    public ResponseEntity<GetSimpleCarListResponse> getSimpleAvailableCarList(){
        return ResponseEntity.ok(service.getSimpleCarList(true));
    }

    @GetMapping("/full/all")
    public ResponseEntity<GetFullCarListResponse> getFullCarList(){
        return ResponseEntity.ok(service.getFullCarList(false));
    }

    @GetMapping("full/available")
    public ResponseEntity<GetFullCarListResponse> getFullAvailableCarList(){
        return ResponseEntity.ok(service.getFullCarList(true));
    }
}
