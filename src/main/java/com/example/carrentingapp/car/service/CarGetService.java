package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.CarLock;
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

    final BaseCarRepository repository;

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
                            car.getPower(),
                            car.getActiveLock()
                    )
            );
        }
        return new GetSimpleCarListResponse(output);
    }

    public GetFullCarListResponse getFullCarList(Boolean onlyAvailable){
        List<BaseCar> cars = onlyAvailable ? repository.findByIsAvailable(true) : repository.findAll();
        List<FullCar> output = new ArrayList<>();

        for(BaseCar car : cars){
            output.add(
                    new FullCar(
                            car.getBrand(),
                            car.getModel(),
                            car.getYearOfProduction(),
                            car.getPower(),
                            car.getTorque(),
                            car.getEngineSize(),
                            car.getMinRankOfUser(),
                            car.getPricePerDay(),
                            car.getHasActiveSale(),
                            car.getIsAvailable(),
                            car.getMileage(),
                            car.getIsRented(),
                            car.getActiveLock()
                    )
            );
        }
        return new GetFullCarListResponse(output);
    }


    public record SimpleCar(
            String brand,
            String model,
            Integer yearOfProduction,
            Float power,
            CarLock lock
    ){}

    public record FullCar(
            String brand,
            String model,
            Integer yearOfProduction,
            Float power,
            Float torque,
            Float engineSize,
            Float minRankOfUser,
            Float pricePerDay,
            Boolean hasActiveSale,
            Boolean isAvailable,
            Float mileage,
            Boolean isRented,
            CarLock lock
    ){}
}
