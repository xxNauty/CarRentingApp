package com.example.carrentingapp.authentication.service;

import com.example.carrentingapp.exception.exception.http_error_500.InvalidDataException;
import com.example.carrentingapp.exception.exception.http_error_500.InvalidEmailAddressException;
import com.example.carrentingapp.exception.exception.http_error_500.PasswordNotSafeException;
import org.springframework.stereotype.Service;

@Service
public class UserDataValidationService {

    public String isEmailCorrect(String email){
        final String pattern = "^[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}$";

        if(!email.matches(pattern)){
            throw new InvalidEmailAddressException("Given email address is incorrect");
        }

        return email;
    }

    public String dataMatchesRequirements(String data, String valueName){
        final String pattern = "[a-zA-Z-]+";
        if(!data.matches(pattern)){
            throw new InvalidDataException("Given " + valueName + " does not match requirements");
        }

        return data;
    }

    //10 znaków, 1 duża litera, 1 mała litera, 1 cyfra, 1 znak specjalny
    public String isPasswordStrongEnough(String password){
        final String pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$&*]).{10,}$";
        if(!password.matches(pattern)){
            throw new PasswordNotSafeException("Given password is not safe enough");
        }

        return password;
    }

}
