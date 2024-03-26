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
        request.checkInput();
        CarBase car = CarBase.builder()
                .brand(request.brand.get())
                .model(request.model.get())
                .yearOfProduction(request.yearOfProduction.get())
                .mileage(request.mileage.get())
                .power(request.power.get())
                .torque(request.torque.get())
                .engineSize(request.engineSize.get())
                .averageFuelConsumption(request.averageFuelConsumption.get())
                .minRankOfUser(request.mileage.get())
                .pricePerDay(request.pricePerDay.get())
                .hasActiveSale(false)
                .status(CarBase.CarStatus.CAR_READY)
                .build();

        carRepository.save(car);

        return new CarResponse(
                car.getId().toString(),
                "Car created"
        );
    }

}
