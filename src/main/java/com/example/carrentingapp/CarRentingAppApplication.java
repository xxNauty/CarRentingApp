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
    public CommandLineRunner commandLineRunner(
            UserCreateService userCreateService,
            UserBaseRepository baseUserRepository
    ){
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

            //todo wymyślić lepiej
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
       8. Przedłużanie blokady samochodu/usera -> stara traci ważność, tylko jedna aktywna w danym momencie
       9. Obsługa błędów UniqueConstraint
       16. Do listy wszystkich samochodów dodać wyświetlanie informacji przy niedostępnych o powodzie i dacie dostępności
       17. Usystematyzować konstrukcję relacji
       22. Wynajęcie samochodu conajmniej 7 dni przed
       23. Naprawić status tokenu weryfikacyjnego dla emaila, dodać status resent przy ponownym wysłaniu maila weryfikacyjnego
       24. Poprawić powiadomienie o blokadzie na zawsze: "Your account has been locked forever..."
       25. Dodać do wszystkich float'ów precyzję do 2 miejsc po przecinku, również w bazie danych
       26. Pobieranie danych samochodu po id: w przypadku błędnego id 404 error
       27. Przerobić wszystkie requesty na wzór z CarRentRequest, dodać walidację danych
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