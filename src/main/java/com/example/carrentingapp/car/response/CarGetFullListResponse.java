package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.service.CarGetService;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class CarGetFullListResponse {
    public List<CarGetService.FullCar> cars;
}
