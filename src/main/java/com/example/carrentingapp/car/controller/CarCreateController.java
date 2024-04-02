package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarCreateService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/car/create")
@PreAuthorize("hasRole('ADMIN')")
public class CarCreateController {

    private final CarCreateService carCreateService;

    @PostMapping("/base")
    public ResponseEntity<CarResponse> createCar(
            @RequestBody CarCreateRequest request
    ) {
        return ResponseEntity.ok(carCreateService.createCar(request));
    }

}
