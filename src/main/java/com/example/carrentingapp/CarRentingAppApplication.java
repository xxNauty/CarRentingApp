package com.example.carrentingapp;

import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import com.example.carrentingapp.user.service.UserCreateService;
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

    @Bean
    public CommandLineRunner commandLineRunner(UserCreateService userCreateService, UserBaseRepository baseUserRepository){
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

            UserBase user = userCreateService.createUser(
                    "Jan",
                    "Nowak",
                    "jan@nowak.pl",
                    "Qwerty123!",
                    LocalDate.now()
            );
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
       22. Wynajęcie samochodu conajmniej 7 dni przed
       28. Przygotowywanie danych do testów
       29. Rozróżnienie przedłużenia blokady i skrócenia, dodać status USER_LOCK_SHORTENED
       30. Endpoint z pobieraniem aktywnej blokady usera, dostępny dla admina
       31. Endpoint z możliwością edycji danych usera
       32. Usunąć osobny endpoint z edycją przebiegu samochodu
       33. GetAllCars -> dodać informacje przy wynajętych do kiedy wynajęty
       34. Brak możliwości wynajęcia zablokowanego lub wynajętego samochodu samochodu
       35. CarRent -> CollectionDate usunąć, datę odbioru ustawić jako RentedFrom
       36. Dzień przed zwrotem przypomnienie mailowe
       37. Automatyczne zmiany statusów
       38. Poprawki zabezpieczeń
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
 */