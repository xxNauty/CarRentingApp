package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.service.CarGetService;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetSimpleCarListResponse {

    private List<CarGetService.SimpleCar> cars;
}
