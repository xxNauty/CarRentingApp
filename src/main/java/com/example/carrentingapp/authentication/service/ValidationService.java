package com.example.carrentingapp.authentication.service;

import java.time.LocalDate;
import java.time.Period;

public class ValidationService {

    protected static boolean isEmailCorrect(String email){
        final String pattern = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";

        return email.matches(pattern);
    }

    protected static boolean isAlphaNumericOrContains(String data){
        final String pattern = "[a-zA-Z-]+";
        return data.matches(pattern);
    }

    //10 znaków, 1 duża litera, 1 mała litera, 1 cyfra, 1 znak specjalny
    protected static boolean isPasswordStrongEnough(String password){
        final String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$&*]).{10,}$";
        return password.matches(pattern);
    }
}
