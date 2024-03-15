package com.example.carrentingapp;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarCreateService;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.service.UserCreateService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommonFunctionsProvider {

    @Autowired
    private final CarCreateService carCreateService;

    @Autowired
    private final UserCreateService userCreateService;


    public String getBearerToken(String email, String password, Integer randomServerPort, TestRestTemplate restTemplate) throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(url);

        LoginRequest request = new LoginRequest(
                email,
                password
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    public UUID createCarForTest(){
        CarResponse response =  carCreateService.createCar(
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

        return response.getId();
    }

    public BaseUser createUser(){
        return userCreateService.createUser(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );
    }

    public BaseUser createAdmin(){
        return userCreateService.createAdmin(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );
    }

}
