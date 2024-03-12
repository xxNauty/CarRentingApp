package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.GetCarResponse;
import com.example.carrentingapp.car.response.GetFullCarListResponse;
import com.example.carrentingapp.car.response.GetSimpleCarListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarGetService {

    private final CarRepository repository;

    public GetCarResponse getCarById(UUID id){
        return new GetCarResponse(repository.findById(id).orElseThrow());
    }

    public GetSimpleCarListResponse getSimpleCarList(Boolean onlyAvailable){
        List<BaseCar> cars = onlyAvailable ? repository.findByIsAvailable(true) : repository.findAll();
        List<SimpleCar> output = new ArrayList<>();

        for(BaseCar car : cars){
            output.add(
                    new SimpleCar(
                            car.getBrand(),
                            car.getModel(),
                            car.getYearOfProduction(),
                            car.getPower()
                    )
            );
        }
        return new GetSimpleCarListResponse(output);
    }

    public GetFullCarListResponse getFullCarList(Boolean onlyAvailable){
        return onlyAvailable
                ? new GetFullCarListResponse(repository.findByIsAvailable(true))
                : new GetFullCarListResponse(repository.findAll());
    }


    public record SimpleCar(
            String brand,
            String model,
            Integer yearOfProduction,
            Float power
    ){}
}
