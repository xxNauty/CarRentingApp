package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.request.CarUpdateDataRequest;
import com.example.carrentingapp.car.request.CarUpdateMileageRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarUpdateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car/update")
@PreAuthorize("hasRole('ADMIN')")
public class CarUpdateDataController {

    private final CarUpdateService carUpdateService;

    @PostMapping("/mileage")
    public ResponseEntity<CarResponse> updateCarMileage(
            @RequestBody CarUpdateMileageRequest request
    ) {
        return ResponseEntity.ok(carUpdateService.updateMileageFromRequest(request));
    }

    @PostMapping("/data")
    public ResponseEntity<CarResponse> updateCarData(
            @RequestBody CarUpdateDataRequest request
    ) {
        return ResponseEntity.ok(carUpdateService.updateCarDataResponse(request));
    }
}
