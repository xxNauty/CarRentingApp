package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.response.CarResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarCreateService {

    private final BaseCarRepository carRepository;

    public CarResponse createCar(
            String brand,
            String model,
            Integer yearOfProduction,
            Float mileage,
            Float power,
            Float torque,
            Float engineSize,
            Float averageFuelConsumption,
            Float minRankOfUser,
            Float pricePerDay
    ){
        BaseCar car = BaseCar.builder()
                .brand(brand)
                .model(model)
                .yearOfProduction(yearOfProduction)
                .mileage(mileage)
                .power(power)
                .torque(torque)
                .engineSize(engineSize)
                .averageFuelConsumption(averageFuelConsumption)
                .minRankOfUser(minRankOfUser)
                .pricePerDay(pricePerDay)
                .hasActiveSale(false)
                .status(BaseCar.CarStatus.CAR_READY)
                .build();

        carRepository.save(car);

        return new CarResponse(
                car.getId(),
                "Car created"
        );
    }

}
