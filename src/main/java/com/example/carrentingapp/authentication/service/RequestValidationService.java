package com.example.carrentingapp.authentication.service;

import com.example.carrentingapp.exception.exception.http_error_500.InvalidArgumentException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Service
public class RequestValidationService extends ValidationService{

    //walidacja czy email jest poprawnym adresem email
    public static void validateEmail(String email){
        if(email.isBlank() || email.isEmpty()){
            throw new InvalidArgumentException("Email cannot be neither blank nor empty");
        }

        if(!isEmailCorrect(email)){
            throw new InvalidArgumentException("Given email is not correct");
        }
    }

    //walidacja czy hasło spełnia minimalne wymagania bezpieczeństwa
    public static void validatePassword(String password){
        if(password.isBlank() || password.isEmpty()){
            throw new InvalidArgumentException("Password cannot be neither blank nor empty");
        }

        if(!isPasswordStrongEnough(password)){
            throw new InvalidArgumentException("Password does not match minimal requirements");
        }
    }

    //walidacja czy użytkownik ma minimalny wymagany wiek
    public static void validateAge(LocalDate date){
        if (Period.between(date, LocalDate.now()).getYears() < 18){
            throw new InvalidArgumentException("You are not old enough to create your own account");
        }
    }

    //walidacja tokenów opierających się o UUID
    public static void validateToken(String uuid){
        UUID.fromString(uuid);
    }

    public static void validateName(String name){
        if(name.isBlank() || name.isEmpty()){
            throw new InvalidArgumentException("Name cannot be neither blank nor empty");
        }

        if(name.length() < 3){
            throw new InvalidArgumentException("Name must be at least 3 character long");
        }

        if(!isAlphaNumericOrContains(name)){
            throw new InvalidArgumentException("Name can contain only alphanumeric characters or hyphen character");
        }
    }

    //walidacja innych parametrów, nie wymagających specjalistycznej walidacji
    public static void validateParameter(String stringParameter){
        if(stringParameter.isBlank() || stringParameter.isEmpty()){
            throw new InvalidArgumentException("Parameter cannot be neither blank nor empty");
        }

        if(stringParameter.length() < 3){
            throw new InvalidArgumentException("Input value must be at least 3 character long");
        }
    }

    //todo: zaimplementować walidację reszty typów
    public static void validateParameter(Integer integerParameter){

    }

    public static void validateParameter(Float floatParameter){

    }

    public static void validateParameter(LocalDate localDateParameter){

    }

    public static void validateParameter(Boolean booleanParameter){

    }
}
