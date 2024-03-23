package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.request.CarUpdateDataRequest;
import com.example.carrentingapp.car.request.CarUpdateMileageRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CarUpdateService {

    private final CarBaseRepository carRepository;

    public CarResponse updateMileageFromRequest(CarUpdateMileageRequest request){
        CarBase car = carRepository.findById(request.getCarId()).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        car.setMileage(car.getMileage() + request.getMileageToAdd());

        carRepository.save(car);

        return new CarResponse(car.getId(), "Car mileage updated, actual mileage: " + car.getMileage());
    }

    public CarResponse updateCarDataResponse(CarUpdateDataRequest request){
        CarBase carToUpdate = carRepository.findById(request.getId()).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

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
