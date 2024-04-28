package com.example.carrentingapp;

import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.service.UserCreateService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class CarRentingAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarRentingAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserCreateService userCreateService, UserBaseRepository baseUserRepository) {
        return args -> {
            System.out.println("\n--------------------------------\n");

            UserBase admin = userCreateService.createAdmin(
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

            UserBase user = userCreateService.createUser(new RegistrationRequest(
                    Optional.of("Jan"),
                    Optional.of("Nowak"),
                    Optional.of("jan@nowak.pl"),
                    Optional.of("Qwerty123!"),
                    Optional.of(LocalDate.now())
            )).getUserBase();

            System.out.println("User created");
            System.out.println("Login: " + user.getEmail());
            System.out.println("Password: Qwerty123!");

            user.setStatus(UserBase.UserStatus.USER_READY);
            baseUserRepository.save(user);

            System.out.println("\n--------------------------------\n");
            System.out.println("Application ready!");
            System.out.println("\n--------------------------------\n");
        };
    }
}
/*
    Lista zadań:
        1. Endpoint zmiana hasła
        2. Testy wynajęcia samochodu
        3. Testy brakującego endpointu getCars /full/all
 */
/*
    Do doczytania:
        1. FetchType Lazy vs Eager
        2. Wygląd testów jednostkowych
        3. "Mockowanie"
        4. Zasada działania regexa
        5. Konwencja nazewnictwa
        6. Dependency injection
        7. np. String data; vs String data = "";
        8. Wzorzec fasada
        9. Spring JPA/Hibernate
        10. Cache'owanie
        11. @Scheduled, crontab
        12. try, catch, FINALLY
        13. Http header X-COM-PERSIST, co to
        14. REGEX
        15. Async
        16. Consumer
        17. Hibernate vs JPA
 */