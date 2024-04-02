package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.car.CarBase;
import com.example.carrentingapp.car.CarBaseRepository;
import com.example.carrentingapp.configuration.service.SecurityService;
import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.car_collected.CarCollectedRequest;
import com.example.carrentingapp.email.notifications.car_rented.CarRentedRequest;
import com.example.carrentingapp.email.notifications.car_returned.CarReturnedRequest;
import com.example.carrentingapp.exception.exception.http_error_409.CarNotAvailableException;
import com.example.carrentingapp.exception.exception.http_error_409.AlreadyDoneException;
import com.example.carrentingapp.email.notifications.rent_checked.RentCheckedRequest;
import com.example.carrentingapp.exception.exception.http_error_403.CarNotReadyException;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import com.example.carrentingapp.exception.exception.http_error_404.CarNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.CarRentNotFoundException;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import com.example.carrentingapp.rent.request.*;
import com.example.carrentingapp.rent.response.*;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.service.UserLockService;
import com.example.carrentingapp.rent.request.CarCollectRequest;
import com.example.carrentingapp.rent.request.CarReturnRequest;
import com.example.carrentingapp.rent.response.CarRentResponse;
import com.example.carrentingapp.rent.request.CarRentRequest;
import com.example.carrentingapp.rent.response.CarCollectResponse;
import com.example.carrentingapp.rent.response.CarReturnResponse;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarRentService {

    private final CarRentRepository carRentRepository;
    private final CarBaseRepository baseCarRepository;
    private final UserBaseRepository baseUserRepository;
    private final SecurityService securityService;
    private final EmailNotificationSender notificationSender;
    private final UserLockService userLockService;

    @Transactional
    public CarRentResponse rentCar(CarRentRequest request) {
        request.checkInput();

        if(Period.between(request.rentedFrom.get(), LocalDate.now()).getYears() < 7){
            throw new InvalidArgumentException("You have to make a reservation at least 7 days earlier");
        }

        CarBase carToRent = baseCarRepository.findById(UUID.fromString(request.carId.get())).orElseThrow(() -> new CarNotFoundException("There is no car with given id"));

        if(!carToRent.getStatus().equals(CarBase.CarStatus.CAR_READY)){
            throw new CarNotAvailableException("This car is currently not available to rent");
        }

        UserBase user = securityService.getLoggedInUser();

        if (Period.between(request.rentedFrom.get(), request.rentedTo.get()).getDays() > 14) {
            throw new InvalidArgumentException("You cannot rent a car for such a long period");
        }

        CarRent rent = new CarRent(
                carToRent,
                user,
                request.rentedFrom.get(),
                request.rentedTo.get()
        );
        carToRent.setStatus(CarBase.CarStatus.CAR_RENTED);


        carRentRepository.save(rent);
        baseCarRepository.save(carToRent);

        notificationSender.sendCarRentedNotification(new CarRentedRequest(carToRent, user));


        return new CarRentResponse(
                "Car rented successfully, we will give you an email information when the car will be ready to collect",
                rent.getId()
        );
    }

    @Transactional
    public CarCollectResponse collectCar(CarCollectRequest request) {
        request.checkInput();
        CarRent rent = carRentRepository.findById(UUID.fromString(request.carRentId.get())).orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));
        UserBase user = rent.getUser();

        if (rent.getStatus().equals(CarRent.CarRentStatus.CAR_RENT_CAR_COLLECTED)) {
            throw new AlreadyDoneException("You have already collected this car");
        }

        if (rent.getRentedFrom().isAfter(LocalDate.now())) {
            throw new CarNotAvailableException("Your car is not ready to collect yet");
        }

        rent.setStatus(CarRent.CarRentStatus.CAR_RENT_CAR_COLLECTED);
        user.setStatus(UserBase.UserStatus.USER_HAS_CAR);

        carRentRepository.save(rent);
        baseUserRepository.save(user);

        notificationSender.sendCarCollectedNotification(new CarCollectedRequest(rent));

        return new CarCollectResponse("Car collected, enjoy your ride");
    }

    @Transactional
    public CarReturnResponse returnCar(CarReturnRequest request) {
        request.checkInput();
        CarRent rent = carRentRepository.findById(UUID.fromString(request.carRentId.get()))
                .orElseThrow(() -> new CarRentNotFoundException("CarRent not found"));

        if (rent.getStatus().isIn(
                CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_OK,
                CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_DAMAGED_CAR,
                CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_LATE,
                CarRent.CarRentStatus.CAR_RENT_WAITING_FOR_REVIEW
        )) {
            throw new AlreadyDoneException("You cannot return already returned car");
        }

        UserBase user = rent.getUser();
        CarBase car = rent.getCar();
        float userRank = user.getRank();

        //modyfikowanie oceny użytkownika na podstawie czasu zwrotu samochodu
        if (rent.getRentedTo().isBefore(LocalDate.now())) {
            rent.setStatus(CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_LATE);
            int daysOfDelay = Period.between(rent.getRentedTo(), LocalDate.now()).getDays();
            if (daysOfDelay > 1) {
                userRank -= daysOfDelay * 0.2F;
            }
            user.carReturnDelaysIncrement();
            //blokowanie na miesiąc przy 5 opóźnieniu
            if (user.getCarReturnDelays() > 4) {
                userLockService.lockUser(new LockRequest(
                        Optional.of(user.getId().toString()),
                        Optional.of(UserLock.LockType.TEMPORARY.name()),
                        Optional.of(UserLock.Reason.FREQUENT_DELAYED_RETURNS.name()),
                        Optional.of(LocalDate.now().plusMonths(1))
                ));
            }
        } else {
            rent.setStatus(CarRent.CarRentStatus.CAR_RENT_WAITING_FOR_REVIEW);
        }

        user.setRank(userRank);
        car.setStatus(CarBase.CarStatus.CAR_READY);
        car.setMileage(car.getMileage() + request.kilometersTraveled.get());

        carRentRepository.save(rent);
        baseCarRepository.save(car);
        baseUserRepository.save(user);

        notificationSender.sendCarReturnedNotification(new CarReturnedRequest(user));

        return new CarReturnResponse("Car returned successfully");
    }

    @Transactional
    public CarCheckAfterRentResponse checkCarAfterRent(CarCheckAfterRentRequest request) {
        request.checkInput();
        CarRent rent = carRentRepository.findById(UUID.fromString(request.carRentId.get())).orElseThrow(() -> new CarRentNotFoundException("Invalid car rent id"));
        UserBase user = rent.getUser();

        if (request.isDamaged.get()) {
            rent.setStatus(CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_DAMAGED_CAR);
            user.updateRank(-1F);
        } else {
            rent.setStatus(CarRent.CarRentStatus.CAR_RENT_END_OF_RENT_OK);
            user.updateRank(0.5F);
        }

        notificationSender.sendRentCheckedNotification(new RentCheckedRequest(rent));

        carRentRepository.save(rent);
        baseUserRepository.save(user);

        return new CarCheckAfterRentResponse("Rent checked successfully");
    }

}
