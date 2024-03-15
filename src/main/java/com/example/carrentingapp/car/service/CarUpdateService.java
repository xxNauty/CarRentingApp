package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.request.UpdateCarDataRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarUpdateService {

    private final BaseCarRepository carRepository;

    public CarResponse updateMileageFromRequest(BaseCar car, float valueToAdd){
        car.setMileage(car.getMileage() + valueToAdd);

        carRepository.save(car);

        return new CarResponse(car.getId(), "Car mileage updated, actual mileage: " + car.getMileage());
    }

    public CarResponse updateCarDataResponse(UpdateCarDataRequest request){
        BaseCar carToUpdate = carRepository.findById(request.getId()).orElseThrow(() -> new CarNotFoundException("There is no car with given Id"));

        carToUpdate.setBrand(request.getBrand());
        carToUpdate.setModel(request.getModel());
        carToUpdate.setYearOfProduction(request.getYearOfProduction());
        carToUpdate.setPower(request.getPower());
        carToUpdate.setTorque(request.getTorque());
        carToUpdate.setEngineSize(request.getEngineSize());
        carToUpdate.setAverageFuelConsumption(request.getAverageFuelConsumption());
        carToUpdate.setMinRankOfUser(request.getMinRankOfUser());
        carToUpdate.setPricePerDay(request.getPricePerDay());

        carRepository.save(carToUpdate);

        return new CarResponse(request.getId(), "Car updated");
    }

}
