package com.example.carrentingapp.email.notifications;

import com.example.carrentingapp.email.notifications.account_locked.AccountLockedNotificationSender;
import com.example.carrentingapp.email.notifications.account_locked.AccountLockedRequest;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedNotificationSender;
import com.example.carrentingapp.email.notifications.account_unlocked.AccountUnlockedRequest;
import com.example.carrentingapp.email.notifications.car_collected.CarCollectedNotificationSender;
import com.example.carrentingapp.email.notifications.car_collected.CarCollectedRequest;
import com.example.carrentingapp.email.notifications.car_ready_to_collect.CarReadyToCollectNotificationSender;
import com.example.carrentingapp.email.notifications.car_ready_to_collect.CarReadyToCollectRequest;
import com.example.carrentingapp.email.notifications.car_rented.CarRentedNotificationSender;
import com.example.carrentingapp.email.notifications.car_rented.CarRentedRequest;
import com.example.carrentingapp.email.notifications.car_returned.CarReturnedNotificationSender;
import com.example.carrentingapp.email.notifications.car_returned.CarReturnedRequest;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailNotificationSender;
import com.example.carrentingapp.email.notifications.confirm_email.ConfirmEmailRequest;
import com.example.carrentingapp.email.notifications.forgot_password.ForgotPasswordNotificationSender;
import com.example.carrentingapp.email.notifications.forgot_password.ForgotPasswordRequest;
import com.example.carrentingapp.email.notifications.rent_going_to_finish.RentGoingToFinishNotificationSender;
import com.example.carrentingapp.email.notifications.rent_going_to_finish.RentGoingToFinishRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailNotificationSender {

    private final AccountLockedNotificationSender accountLockedNotificationSender;
    private final AccountUnlockedNotificationSender accountUnlockedNotificationSender;
    private final CarCollectedNotificationSender carCollectedNotificationSender;
    private final CarReadyToCollectNotificationSender carReadyToCollectNotificationSender;
    private final CarRentedNotificationSender carRentedNotificationSender;
    private final CarReturnedNotificationSender carReturnedNotificationSender;
    private final ConfirmEmailNotificationSender confirmEmailNotificationSender;
    private final ForgotPasswordNotificationSender forgotPasswordNotificationSender;
    private final RentGoingToFinishNotificationSender rentGoingToFinishNotificationSender;

    public void sendAccountLockedNotification(AccountLockedRequest request){
        accountLockedNotificationSender.sendEmail(request);
    }

    public void sendAccountUnlockedNotification(AccountUnlockedRequest request){
        accountUnlockedNotificationSender.sendEmail(request);
    }

    public void sendCarCollectedNotification(CarCollectedRequest request){
        carCollectedNotificationSender.sendEmail(request);
    }

    public void sendCarReadyToCollectNotification(CarReadyToCollectRequest request){
        carReadyToCollectNotificationSender.sendEmail(request);
    }

    public void sendCarRentedNotification(CarRentedRequest request){
        carRentedNotificationSender.sendEmail(request);
    }

    //todo: do dokończenia
    public void sendCarReturnedNotification(CarReturnedRequest request){
        carReturnedNotificationSender.sendEmail(request);
    }

    public void sendConfirmEmailNotification(ConfirmEmailRequest request){
        confirmEmailNotificationSender.sendEmail(request);
    }

    public void sendForgotPasswordNotification(ForgotPasswordRequest request){
        forgotPasswordNotificationSender.sendEmail(request);
    }

    public void sendRentGoingToFinishNotification(RentGoingToFinishRequest request){
        rentGoingToFinishNotificationSender.sendEmail(request);
    }
}
