package com.example.carrentingapp.rent.controller;

import com.example.carrentingapp.rent.request.CarRentRequest;
import com.example.carrentingapp.rent.request.CollectCarRequest;
import com.example.carrentingapp.rent.request.ReturnCarRequest;
import com.example.carrentingapp.rent.response.CarRentResponse;
import com.example.carrentingapp.rent.response.CollectCarResponse;
import com.example.carrentingapp.rent.response.ReturnCarResponse;
import com.example.carrentingapp.rent.service.CarRentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
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
        return ResponseEntity.ok(carRentService.carReadyToCollect(request));
    }

    @PostMapping("/return")
    public ResponseEntity<ReturnCarResponse> returnCar(
            @RequestBody ReturnCarRequest request
    ){
        return ResponseEntity.ok(carRentService.returnCar(request));
    }
}
