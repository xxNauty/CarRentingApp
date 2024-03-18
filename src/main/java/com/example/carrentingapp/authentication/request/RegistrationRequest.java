package com.example.carrentingapp.authentication.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class RegistrationRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
}
