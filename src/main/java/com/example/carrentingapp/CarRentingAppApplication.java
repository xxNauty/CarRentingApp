package com.example.carrentingapp;

import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.UserCreatorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

//todo: usunąć wszystkie var

//todo: naprawić security, wszystkie url mają domyślnie wymagać autoryzacji,
// tylko wybrane mogą być dostępne publicznie (odwrócić aktualną sytuację)

@SpringBootApplication
public class CarRentingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentingAppApplication.class, args);
    }

    @Bean //Rozwiązanie na czas tworzenia aplikacji
    public CommandLineRunner commandLineRunner(UserCreatorService service){
        return args -> {
            BaseUser admin = service.createAdmin(
                    "Adam",
                    "Kowalski",
                    "adam@kowalski.pl",
                    "Qwerty123",
                    LocalDate.now()
            );
            System.out.println("Admin created");
            System.out.println("Login: " + admin.getEmail());
            System.out.println("Password: Qwerty123");

            BaseUser user = service.createAdmin(
                    "Jan",
                    "Nowak",
                    "jan@nowak.pl",
                    "Qwerty123",
                    LocalDate.now()
            );
            System.out.println("User created");
            System.out.println("Login: " + user.getEmail());
            System.out.println("Password: Qwerty123");
        };
    }

}
