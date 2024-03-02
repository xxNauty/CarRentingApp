package com.example.carrentingapp;

import com.example.carrentingapp.user.UserCreatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class CarRentingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentingAppApplication.class, args);
    }

    @Bean //Rozwiązanie na czas tworzenia aplikacji
    public CommandLineRunner commandLineRunner(UserCreatorService service){
        return args -> {
            service.createAdmin(new UserCreatorService.UserCreationData(
                    "Adam",
                    "Kowalski",
                    "adam@kowalski.pl",
                    "Qwerty123",
                    LocalDate.now()
            ));
            System.out.println("Admin created");
        };
    }

}
