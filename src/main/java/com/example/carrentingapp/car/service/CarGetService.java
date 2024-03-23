package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.response.CarGetResponse;
import com.example.carrentingapp.car.response.CarGetFullListResponse;
import com.example.carrentingapp.car.response.CarGetSimpleListResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarGetService {

    private final CarBaseRepository repository;

    public CarGetResponse getCarById(UUID id){
        return new CarGetResponse(repository.findById(id).orElseThrow());
    }

    public CarGetSimpleListResponse getSimpleCarList(Boolean onlyAvailable){
        List<CarBase> cars = onlyAvailable ? repository.findByStatus(CarBase.CarStatus.CAR_READY) : repository.findAll();
        List<SimpleCar> output = new ArrayList<>();

        for(CarBase car : cars){
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
        return new CarGetSimpleListResponse(output);
    }

    public CarGetFullListResponse getFullCarList(Boolean onlyAvailable){
        List<CarBase> cars = onlyAvailable ? repository.findByStatus(CarBase.CarStatus.CAR_READY)  : repository.findAll();
        List<FullCar> output = new ArrayList<>();

        for(CarBase car : cars){
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
                            car.getMileage(),
                            car.getActiveLock()
                    )
            );
        }
        return new CarGetFullListResponse(output);
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
            Float mileage,
            CarLock lock
    ){}
}
