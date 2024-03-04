package com.example.carrentingapp;

import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.user.UserCreatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootApplication
public class CarRentingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentingAppApplication.class, args);

        BaseCar car = new BaseCar(
                UUID.randomUUID(),
                "Opel",
                "Corsa",
                2022,
                0F,
                false,
                105.3F,
                123F,
                1.2F,
                6.7F,
                6.0F,
                100F,
                false
        );

        System.out.println(car.getId() instanceof UUID);

    }

    @Bean //Rozwiązanie na czas tworzenia aplikacji
    public CommandLineRunner commandLineRunner(UserCreatorService service){
        return args -> {
            service.createAdmin(
                    "Adam",
                    "Kowalski",
                    "adam@kowalski.pl",
                    "Qwerty123",
                    LocalDate.now()
            );
            System.out.println("Admin created");
        };
    }

}
