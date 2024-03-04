package com.example.carrentingapp.car;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarCreatorService {

    private final CarRepository carRepository;

    public BaseCar createCar(
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
                .isRented(false)
                .power(power)
                .torque(torque)
                .engineSize(engineSize)
                .averageFuelConsumption(averageFuelConsumption)
                .minRankOfUser(minRankOfUser)
                .pricePerDay(pricePerDay)
                .hasActiveSale(false)
                .build();

        carRepository.save(car);

        return car;
    }

}
