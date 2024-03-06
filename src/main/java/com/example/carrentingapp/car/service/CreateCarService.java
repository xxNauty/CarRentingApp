package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.CarRepository;
import com.example.carrentingapp.car.response.CarResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateCarService {

    private final CarRepository carRepository;

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
                .isRented(false)
                .power(power)
                .torque(torque)
                .engineSize(engineSize)
                .averageFuelConsumption(averageFuelConsumption)
                .minRankOfUser(minRankOfUser)
                .pricePerDay(pricePerDay)
                .hasActiveSale(false)
                .isAvailable(true)
                .build();

        carRepository.save(car);

        return new CarResponse(
                car.getId(),
                "Car created"
        );
    }

}
