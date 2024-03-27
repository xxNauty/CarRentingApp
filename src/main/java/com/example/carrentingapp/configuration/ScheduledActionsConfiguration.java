package com.example.carrentingapp.configuration;

import com.example.carrentingapp.car.service.CarUnlockService;
import com.example.carrentingapp.rent.service.CarReadyToCollectService;
import com.example.carrentingapp.user.service.UserUnlockService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@AllArgsConstructor
public class ScheduledActionsConfiguration {

    private final CarReadyToCollectService carReadyToRentService;
    private final UserUnlockService userUnlockService;
    private final CarUnlockService carUnlockService;

    @Scheduled(cron = "0 0 8 * * 1-5") //raz dziennie, od poniedziałku do piątku, o 8:00 rano
    public void checkIfThereIsAnyCarReadyToCollect(){
        carReadyToRentService.check();
    }

    @Scheduled(cron = "0 10 8 * * 1-5") //raz dziennie, od poniedziałku do piątku, o 8:10 rano
    public void checkIfThereIsAnyUserToUnlock(){
        userUnlockService.check();
    }

    @Scheduled(cron = "0 20 8 * * 1-5") //raz dziennie, od poniedziałku do piątku, o 8:20 rano
    public void checkIfThereIsAnyCarToUnlock(){
        carUnlockService.check();
    }

    @Scheduled(cron = "0 30 8 * * *") //raz dziennie, przez cały tydzień, o 8:30 rano
    public void checkIfAnyRentIsGoingToFinish(){

    }
}
