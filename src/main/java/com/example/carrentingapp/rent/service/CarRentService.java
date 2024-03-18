package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.email.notifications.NotificationSender;
import com.example.carrentingapp.email.notifications.car_collected.CarCollectedRequest;
import com.example.carrentingapp.email.notifications.car_rented.CarRentedRequest;
import com.example.carrentingapp.email.notifications.car_returned.CarReturnedRequest;
import com.example.carrentingapp.exception.exception.http_error_403.CarNotReadyException;
import com.example.carrentingapp.exception.exception.http_error_500.RentPeriodTooLongException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarRentNotFoundException;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import com.example.carrentingapp.rent.request.CarReadyToCollectRequest;
import com.example.carrentingapp.rent.request.CollectCarRequest;
import com.example.carrentingapp.rent.request.ReturnCarRequest;
import com.example.carrentingapp.rent.response.CarReadyToCollectResponse;
import com.example.carrentingapp.rent.response.CarRentResponse;
import com.example.carrentingapp.rent.request.CarRentRequest;
import com.example.carrentingapp.rent.response.CollectCarResponse;
import com.example.carrentingapp.rent.response.ReturnCarResponse;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final NotificationSender notificationSender;

    @Transactional
    public CarRentResponse rentCar(CarRentRequest request){
        BaseCar carToRent = baseCarRepository.findById(UUID.fromString(request.getCarId())).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        BaseUser user = securityService.getLoggedInUser();

        if(Period.between(request.getRentedFrom(), request.getRentedTo()).getDays() > 14){
            throw new RentPeriodTooLongException("You cannot rent a car for such a long period");
        }

        boolean isActive = true; //todo: do poprawy
        LocalDateTime collectionDate  = LocalDateTime.now().plusDays(1);

        CarRent rent = new CarRent(
                carToRent,
                user,
                request.getRentedFrom(),
                request.getRentedTo(),
                collectionDate
        );
        carToRent.setStatus(BaseCar.CarStatus.CAR_RENTED);


        carRentRepository.save(rent);
        baseCarRepository.save(carToRent);

        notificationSender.sendCarRentedNotification(new CarRentedRequest(carToRent, user));


        String returnMessage = isActive
                ? "Car rented successfully, you can collect your car at: " + collectionDate
                : "Car rented successfully, we will give you an email information when the car will be ready to collect";

        return new CarRentResponse(
                returnMessage,
                isActive,
                rent.getId()
        );
    }

    public CarReadyToCollectResponse carReadyToCollect(CarReadyToCollectRequest request){
        notificationSender.sendCarReadyToCollectNotification(
                new com.example.carrentingapp.email.notifications.car_ready_to_collect.CarReadyToCollectRequest(request.getUser())
        );
        return new CarReadyToCollectResponse("Notification sent");
    }

    @Transactional
    public CollectCarResponse collectCar(CollectCarRequest request){
        CarRent rent = carRentRepository.findById(request.getCarRentId()).orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));
        BaseUser user = rent.getUser();

        if(rent.getCollectionDate().isAfter(LocalDateTime.now())){
            throw new CarNotReadyException("Your car is not ready to collect yet");
        }

        rent.setStatus(CarRent.CarRentStatus.CAR_RENT_CAR_COLLECTED);
        user.setStatus(BaseUser.UserStatus.USER_HAS_CAR);

        carRentRepository.save(rent);
        baseUserRepository.save(user);

        notificationSender.sendCarCollectedNotification(new CarCollectedRequest(rent));

        return new CollectCarResponse("Car collected, enjoy your ride");
    }

    @Transactional
    public ReturnCarResponse returnCar(ReturnCarRequest request){
        CarRent rent = carRentRepository.findById(request.getCarRentId()).orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));
        BaseUser user = rent.getUser();
        BaseCar car = rent.getCar();

        //todo: dorobić mechanizm liczenia rankingu
//        float pointsAfterRent = 0;
//        int late = Period.between(rent.getRentedTo(), LocalDate.now()).getDays();
//        if (late > 0){
//            pointsAfterRent -= (late * 0.2F);
//        }
//        else {
//            pointsAfterRent += 0.5F;
//        }
//        user.updateRank(pointsAfterRent);
        rent.setStatus(CarRent.CarRentStatus.CAR_RENT_WAITING_FOR_REVIEW);
        car.setStatus(BaseCar.CarStatus.CAR_READY);
        car.setMileage(car.getMileage() + request.getKilometersTraveled());

        carRentRepository.save(rent);
        baseCarRepository.save(car);
        baseUserRepository.save(user);

        //todo
        notificationSender.sendCarReturnedNotification(new CarReturnedRequest());

        return new ReturnCarResponse("Car returned successfully");
    }

}
