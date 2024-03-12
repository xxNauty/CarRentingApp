package com.example.carrentingapp.car.response;

import com.example.carrentingapp.car.BaseCar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFullCarListResponse {
    public List<BaseCar> cars;
}
