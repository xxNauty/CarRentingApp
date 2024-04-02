package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.service.CarGetService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarGetSimpleListResponse {

    private List<CarGetService.SimpleCar> cars;
}
