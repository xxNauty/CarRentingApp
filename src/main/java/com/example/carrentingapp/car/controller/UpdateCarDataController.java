package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.request.UpdateCarDataRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarUpdateService;
import com.example.carrentingapp.car.request.UpdateCarMileageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/car/update")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UpdateCarDataController {

    private final CarUpdateService service;

    private final CarRepository repository;

    @PostMapping("/mileage")
    public ResponseEntity<CarResponse> updateCarMileage(
            @RequestBody UpdateCarMileageRequest request
    ) {
        return ResponseEntity.ok(service.updateMileageFromRequest(
                repository.findById(request.getCarId()).orElseThrow(),
                request.getMileageToAdd())
        );
    }

    @PostMapping("/data")
    public ResponseEntity<CarResponse> updateCarData(
            @RequestBody UpdateCarDataRequest request
    ){
        return ResponseEntity.ok(service.updateCarDataResponse(request));
    }
}
