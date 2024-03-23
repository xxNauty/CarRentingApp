package com.example.carrentingapp.rent.controller;

import com.example.carrentingapp.rent.request.*;
import com.example.carrentingapp.rent.response.*;
import com.example.carrentingapp.rent.service.CarRentService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car/rent")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class CarRentController {

    private final CarRentService carRentService;

    @PostMapping("/request")
    public ResponseEntity<CarRentResponse> rentCar(
            @RequestBody CarRentRequest request
    ){
        return ResponseEntity.ok(carRentService.rentCar(request));
    }

    @PostMapping("/collect")
    public ResponseEntity<CollectCarResponse> collectRentedCar(
            @RequestBody CollectCarRequest request
    ){
        return ResponseEntity.ok(carRentService.collectCar(request));
    }

    @PostMapping("/return")
    public ResponseEntity<ReturnCarResponse> returnCar(
            @RequestBody ReturnCarRequest request
    ){
        return ResponseEntity.ok(carRentService.returnCar(request));
    }

    @PostMapping("/return/check")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarCheckAfterRentResponse> checkCarAfterRent(
            @RequestBody CarCheckAfterRentRequest request
    ){
        return ResponseEntity.ok(carRentService.checkCarAfterRent(request));
    }
}
