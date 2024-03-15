package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.service.CarGetService;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFullCarListResponse {
    public List<CarGetService.FullCar> cars;
}
