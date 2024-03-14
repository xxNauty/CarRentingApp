package com.example.carrentingapp;

import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.service.UserCreateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

//todo: naprawić security, wszystkie url mają domyślnie wymagać autoryzacji, tylko wybrane mogą być dostępne publicznie (odwrócić aktualną sytuację)

//todo: dodać obsługę 404 error w przypadku braku znalezionego entity

//todo: zastosować transakcje tam gdzie potrzeba

@SpringBootApplication
public class CarRentingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentingAppApplication.class, args);
    }

    @Bean //Rozwiązanie na czas tworzenia aplikacji
    public CommandLineRunner commandLineRunner(UserCreateService service){
        return args -> {
            System.out.println("\n--------------------------------\n");

            BaseUser admin = service.createAdmin(
                    "Adam",
                    "Kowalski",
                    "adam@kowalski.pl",
                    "Qwerty123!",
                    LocalDate.now()
            );
            System.out.println("Admin created");
            System.out.println("Login: " + admin.getEmail());
            System.out.println("Password: Qwerty123!");

            System.out.println("\n--------------------------------\n");

            BaseUser user = service.createUser(
                    "Jan",
                    "Nowak",
                    "jan@nowak.pl",
                    "Qwerty123!",
                    LocalDate.now()
            );
            System.out.println("User created");
            System.out.println("Login: " + user.getEmail());
            System.out.println("Password: Qwerty123!");

            System.out.println("\n--------------------------------\n");
        };
    }

}
