package com.example.carrentingapp;

import com.example.carrentingapp.car.service.CarCreateService;
import com.example.carrentingapp.user.BaseUser;
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
    public CommandLineRunner commandLineRunner(UserCreateService userCreateService, CarCreateService carCreateService){
        return args -> {
            System.out.println("\n--------------------------------\n");

            BaseUser admin = userCreateService.createAdmin(
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

            BaseUser user = userCreateService.createUser(
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

            for (int i = 0; i < 5; i++) {
                carCreateService.createCar(
                        "Opel",
                        "Corsa",
                        2000,
                        300_000F,
                        50F,
                        90F,
                        1F,
                        5.6F,
                        5F,
                        430.99F
                );
            }
        };
    }
}

/*
    Testy:
        1. Rejestracja działa
        2. Potwierdzanie adresu email działa
        3. Blokowanie i odblokowywanie użytkownika działa
        4. Tworzenie encji samochodu działa
        5. Pobieranie list oraz pojedyńczego samochodu działa
        (Todo: dorobić obsługę isRented do pobierania dostępnych  samochodów, jeśli niedostępny to zwraca powód niedostępności)
        6. Wynajmowanie auta działa dla poprawnych danych
 */
/*
 1.
 2. Dorobić możliwość ponownego wysłania potwierdzenia adresu email
 3. UpdateCarData/UpdateCarMileage -> zwraca 500 zamiast 403 przy wysłaniu zapytania jako User
 4. Zablokować możliwość odbioru auta które jest jeszcze wynajęte
 5. GetUserData zwracający informacje o:
    a) wybranym użytkowniku, gdy odpytuje admin
    b) dane użytkownika odpytującego, dla role user
 6. Brakujące powiadomienia email:
    a) Utworzenie konta
    b) Wynajęcie auta
    c) Auto gotowe do odbioru
    d) Odbiór auta
    e) Zwrot auta i podsumowanie
 7. Liczenie opóźnień w zwrotach, 5 -> blokada na miesiąc
 8. Przedłużanie blokady samochodu/usera -> stara traci ważność, tylko jedna aktywna w danym momencie
 9. Obsługa błędów UniqueConstraint
 10. Testy przepisać na nowo, doczytać o:
    a) wyglądzie testów jednostkowych
    b) czym jest Mockowanie
 11. W UserDataValidationService sprawdzić czy nie ma lepszych opcji niż Regex
 12. Usunąć możliwość podwójnego oddania i odebrania samochodu
 */

