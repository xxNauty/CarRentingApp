package com.example.carrentingapp.car.controller;

import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CreateCarService;
import com.example.carrentingapp.car.request.CreateCarRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/car/create")
@RequiredArgsConstructor
public class CreateCarController {

    private final CreateCarService service;

    @PostMapping("/base")
    public ResponseEntity<CarResponse> createCar(
            @RequestBody CreateCarRequest request
            ){
        return ResponseEntity.ok(service.createCar(
                request.getBrand(),
                request.getModel(),
                request.getYearOfProduction(),
                request.getMileage(),
                request.getPower(),
                request.getTorque(),
                request.getEngineSize(),
                request.getAverageFuelConsumption(),
                request.getMinRankOfUser(),
                request.getPricePerDay()
        ));
    }

}
