package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.response.CarResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CreateCarTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarBaseRepository baseCarRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void correctCreateCarTest(){
        final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";
        final String token = getToken("adam@kowalski.pl");

        CarCreateRequest createCarRequest = new CarCreateRequest(
                Optional.of("Polonez"),
                Optional.of("Caro Plus"),
                Optional.of(1999),
                Optional.of(300_000F),
                Optional.of(104F),
                Optional.of(140F),
                Optional.of(1.4F),
                Optional.of(8F),
                Optional.of(6.5F),
                Optional.of(300.99F)
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");
        createCarHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CarCreateRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

        ResponseEntity<CarResponse> carResponse = testRestTemplate.postForEntity(
                createCarUrl,
                createCarRequestHttpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), carResponse.getStatusCode());
        Assertions.assertNotNull(carResponse.getBody());
        Assertions.assertEquals("Car created", carResponse.getBody().getMessage());

        UUID carId = UUID.fromString(carResponse.getBody().getId());

        CarBase car = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals("Polonez", car.getBrand());
        Assertions.assertFalse(car.getHasActiveSale());
        Assertions.assertEquals(CarBase.CarStatus.CAR_READY, car.getStatus());
    }

    @Test
    public void createCarAsNonPrivilegedUserTest(){
        final int carCountBefore = baseCarRepository.findAll().size();

        final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";
        final String token = getToken("jan@nowak.pl");

        CarCreateRequest createCarRequest = new CarCreateRequest(
                Optional.of("Polonez"),
                Optional.of("Caro Plus"),
                Optional.of(1999),
                Optional.of(300_000F),
                Optional.of(104F),
                Optional.of(140F),
                Optional.of(1.4F),
                Optional.of(8F),
                Optional.of(6.5F),
                Optional.of(300.99F)
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");
        createCarHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CarCreateRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

        ResponseEntity<CarResponse> carResponse = testRestTemplate.postForEntity(
                createCarUrl,
                createCarRequestHttpEntity,
                CarResponse.class
        );

        final int carCountAfter = baseCarRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carResponse.getStatusCode());
        Assertions.assertEquals(carCountBefore, carCountAfter);
    }

    @Test
    public void createCarNotAuthorized(){
        final int carCountBefore = baseCarRepository.findAll().size();

        final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";

        CarCreateRequest createCarRequest = new CarCreateRequest(
                Optional.of("Polonez"),
                Optional.of("Caro Plus"),
                Optional.of(1999),
                Optional.of(300_000F),
                Optional.of(104F),
                Optional.of(140F),
                Optional.of(1.4F),
                Optional.of(8F),
                Optional.of(6.5F),
                Optional.of(300.99F)
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<CarCreateRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

        ResponseEntity<CarResponse> carResponse = testRestTemplate.postForEntity(
                createCarUrl,
                createCarRequestHttpEntity,
                CarResponse.class
        );

        final int carCountAfter = baseCarRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carResponse.getStatusCode());
        Assertions.assertEquals(carCountBefore, carCountAfter);
    }


    private String getToken(String email){
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        LoginRequest loginRequest = new LoginRequest(
                Optional.of(email),
                Optional.of("Qwerty123!")
        );

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> loginRequestHttpEntity = new HttpEntity<>(loginRequest, loginHeaders);

        ResponseEntity<AuthenticationResponse> authenticationResponse = testRestTemplate.postForEntity(
                loginURL,
                loginRequestHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertNotNull(authenticationResponse.getBody());

        return authenticationResponse.getBody().getAccessToken();
    }
}
