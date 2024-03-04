package com.example.carrentingapp.authorization;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.UserRepository;
import org.apache.coyote.Response;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@SpringBootTest
public class AuthorizationTest {

    @Autowired
    private UserRepository repository;

    @Test
    void createUser(){
        long userCountBefore = repository.findAll().size();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/api/v1/auth/register",
                new RegistrationRequest(
                        "Adam",
                        "Kowalski",
                        "adam@kowalski.pl",
                        "Qwerty123",
                        LocalDate.now()
                ),
                AuthenticationResponse.class
        );

        Assertions.assertNotEquals(repository.findAll().size(), userCountBefore);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    }

    @Test
    void login(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                "http://localhost:8080/api/v1/auth/register",
                new LoginRequest(
                        "adam@kowalski.pl",
                        "Qwerty123"
                ),
                AuthenticationResponse.class
        );

        Assertions.assertNotNull(response);
        Assertions.assertEquals(response.getStatusCode(), HttpStatusCode.valueOf(200));
    }

}
