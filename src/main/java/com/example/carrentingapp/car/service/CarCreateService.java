package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.response.CarResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarCreateService {

    private final CarBaseRepository carRepository;

    public CarResponse createCar(CarCreateRequest request){
        CarBase car = CarBase.builder()
                .brand(request.getBrand())
                .model(request.getModel())
                .yearOfProduction(request.getYearOfProduction())
                .mileage(request.getMileage())
                .power(request.getPower())
                .torque(request.getTorque())
                .engineSize(request.getEngineSize())
                .averageFuelConsumption(request.getAverageFuelConsumption())
                .minRankOfUser(request.getMinRankOfUser())
                .pricePerDay(request.getPricePerDay())
                .hasActiveSale(false)
                .status(CarBase.CarStatus.CAR_READY)
                .build();

        carRepository.save(car);

        return new CarResponse(
                car.getId(),
                "Car created"
        );
    }

}
