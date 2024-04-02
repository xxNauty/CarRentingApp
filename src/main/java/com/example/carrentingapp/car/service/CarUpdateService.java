package com.example.carrentingapp.car.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.car.request.CarUpdateDataRequest;
import com.example.carrentingapp.car.request.CarUpdateMileageRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CarUpdateService {

    private final CarBaseRepository carRepository;

    public CarResponse updateMileageFromRequest(CarUpdateMileageRequest request) {
        request.checkInput();
        CarBase car = carRepository.findById(UUID.fromString(request.carId.get())).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        car.setMileage(car.getMileage() + request.mileageToAdd.get());

        carRepository.save(car);

        return new CarResponse(car.getId().toString(), "Car mileage updated, actual mileage: " + car.getMileage());
    }

    public CarResponse updateCarDataResponse(CarUpdateDataRequest request) {
        request.checkInput();
        CarBase carToUpdate = carRepository.findById(UUID.fromString(request.id.get())).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        carToUpdate.setBrand(request.brand.get());
        carToUpdate.setModel(request.model.get());
        carToUpdate.setYearOfProduction(request.yearOfProduction.get());
        carToUpdate.setPower(request.power.get());
        carToUpdate.setTorque(request.torque.get());
        carToUpdate.setEngineSize(request.engineSize.get());
        carToUpdate.setAverageFuelConsumption(request.averageFuelConsumption.get());
        carToUpdate.setMinRankOfUser(request.minRankOfUser.get());
        carToUpdate.setPricePerDay(request.power.get());

        carRepository.save(carToUpdate);

        return new CarResponse(request.id.get(), "Car updated");
    }

}
