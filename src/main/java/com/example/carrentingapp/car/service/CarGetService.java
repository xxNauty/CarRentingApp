package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.GetCarListResponse;
import com.example.carrentingapp.car.response.GetCarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarGetService {

    private final CarRepository repository;

    public GetCarResponse getCarById(UUID id){
        return new GetCarResponse(repository.findById(id).orElseThrow());
    }

    public GetCarListResponse getAllCars(){
        return new GetCarListResponse(repository.findAll());
    }

}
