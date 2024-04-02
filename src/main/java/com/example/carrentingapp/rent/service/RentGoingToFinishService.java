package com.example.carrentingapp.rent.service;

import com.example.carrentingapp.configuration.common_interfaces.ScheduledAction;
import com.example.carrentingapp.email.notifications.EmailNotificationSender;
import com.example.carrentingapp.email.notifications.rent_going_to_finish.RentGoingToFinishRequest;
import com.example.carrentingapp.rent.CarRent;
import com.example.carrentingapp.rent.CarRentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@AllArgsConstructor
public class RentGoingToFinishService implements ScheduledAction {

    private final CarRentRepository carRentRepository;
    private final EmailNotificationSender emailNotificationSender;

    @Override
    public void check() {
        List<CarRent> rents = carRentRepository.getAllByStatus(CarRent.CarRentStatus.CAR_RENT_CAR_COLLECTED);
        for (CarRent rent : rents) {
            if (Period.between(rent.getRentedTo(), LocalDate.now()).getDays() < 2) {
                emailNotificationSender.sendRentGoingToFinishNotification(new RentGoingToFinishRequest(rent.getUser()));
            }
        }
    }
}
