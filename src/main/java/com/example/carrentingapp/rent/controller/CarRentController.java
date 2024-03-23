package com.example.carrentingapp.rent.controller;

import com.example.carrentingapp.rent.request.CarReadyToCollectRequest;
import com.example.carrentingapp.rent.request.CarRentRequest;
import com.example.carrentingapp.rent.request.CarCollectRequest;
import com.example.carrentingapp.rent.request.CarReturnRequest;
import com.example.carrentingapp.rent.response.CarReadyToCollectResponse;
import com.example.carrentingapp.rent.response.CarRentResponse;
import com.example.carrentingapp.rent.response.CarCollectResponse;
import com.example.carrentingapp.rent.response.CarReturnResponse;
import com.example.carrentingapp.rent.service.CarRentService;
import lombok.AllArgsConstructor;
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

    @PostMapping("/collect/ready_to")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarReadyToCollectResponse> carReadyToCollect(
            @RequestBody CarReadyToCollectRequest request
    ){
        return ResponseEntity.ok(carRentService.carReadyToCollect(request));
    }

    @PostMapping("/collect")
    public ResponseEntity<CarCollectResponse> collectRentedCar(
            @RequestBody CarCollectRequest request
    ){
        return ResponseEntity.ok(carRentService.collectCar(request));
    }

    @PostMapping("/return")
    public ResponseEntity<CarReturnResponse> returnCar(
            @RequestBody CarReturnRequest request
    ){
        return ResponseEntity.ok(carRentService.returnCar(request));
    }
}
