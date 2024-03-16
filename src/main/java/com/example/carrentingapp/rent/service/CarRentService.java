package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.exception.exception.http_error_403.BaseAccessDeniedException;
import com.example.carrentingapp.exception.exception.http_error_403.CarNotReadyException;
import com.example.carrentingapp.exception.exception.http_error_403.RentPeriodTooLongException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarRentNotFoundException;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import com.example.carrentingapp.rent.request.CollectCarRequest;
import com.example.carrentingapp.rent.request.ReturnCarRequest;
import com.example.carrentingapp.rent.response.CarRentResponse;
import com.example.carrentingapp.rent.request.CarRentRequest;
import com.example.carrentingapp.rent.response.CollectCarResponse;
import com.example.carrentingapp.rent.response.ReturnCarResponse;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarRentService {

    private final CarRentRepository carRentRepository;
    private final BaseCarRepository baseCarRepository;
    private final BaseUserRepository baseUserRepository;
    private final SecurityService securityService;

    @Transactional
    public CarRentResponse rentCar(CarRentRequest request){
        BaseCar carToRent = baseCarRepository.findById(UUID.fromString(request.getCarId())).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        BaseUser user = securityService.getLoggedInUser();

        if(Period.between(request.getRentedFrom(), request.getRentedTo()).getDays() > 14){
            throw new RentPeriodTooLongException("You cannot rent a car for such a long period");
        }

        boolean isActive = !carToRent.getIsRented();
        LocalDateTime collectionDate  = LocalDateTime.now().plusDays(1);

        CarRent rent = new CarRent(
                carToRent,
                user,
                request.getRentedFrom(),
                request.getRentedTo(),
                collectionDate,
                isActive
        );
        carToRent.setIsRented(true);


        carRentRepository.save(rent);
        baseCarRepository.save(carToRent);


        String returnMessage = isActive
                ? "Car rented successfully, you can collect your car at: " + collectionDate
                : "Car rented successfully, we will give you an email information when the car will be ready to collect";

        return new CarRentResponse(
                returnMessage,
                isActive,
                rent.getId()
        );
    }

    public CollectCarResponse carReadyToCollect(CollectCarRequest request){
        CarRent rent = carRentRepository.findById(request.getCarRentId()).orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));

        if(rent.getCollectionDate().isAfter(LocalDateTime.now())){
            throw new CarNotReadyException("Your car is not ready to collect yet");
        }

        rent.setIsActive(true);
        rent.setCollectedCar(true);
        carRentRepository.save(rent);

        return new CollectCarResponse("Car collected, enjoy your ride");
    }

    @Transactional
    public ReturnCarResponse returnCar(ReturnCarRequest request){
        CarRent rent = carRentRepository.findById(request.getCarRentId()).orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));
        BaseUser user = rent.getUser();
        BaseCar car = rent.getCar();

        float pointsAfterRent = 0;
        int late = Period.between(rent.getRentedTo(), LocalDate.now()).getDays();
        if (late > 0){
            pointsAfterRent -= (late * 0.2F);
        }
        else {
            pointsAfterRent += 0.5F;
        }
        user.updateRank(pointsAfterRent);
        rent.setIsActive(false);
        car.setIsRented(false);
        car.setMileage(car.getMileage() + request.getKilometersTraveled());

        carRentRepository.save(rent);
        baseCarRepository.save(car);
        baseUserRepository.save(user);

        return new ReturnCarResponse("Car returned successfully");
    }

}
