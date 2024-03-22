package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.request.CreateCarRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.service.UserCreateService;
import org.junit.BeforeClass;
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

import java.time.LocalDate;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CreateCarTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BaseCarRepository baseCarRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void correctCreateCarTest(){
        final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";
        final String token = getToken("adam@kowalski.pl");

        CreateCarRequest createCarRequest = new CreateCarRequest(
                "Polonez",
                "Caro Plus",
                1999,
                300_000F,
                104F,
                140F,
                1.4F,
                8F,
                6.5F,
                300.99F
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");
        createCarHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CreateCarRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

        ResponseEntity<CarResponse> carResponse = testRestTemplate.postForEntity(
                createCarUrl,
                createCarRequestHttpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), carResponse.getStatusCode());
        Assertions.assertNotNull(carResponse.getBody());
        Assertions.assertEquals("Car created", carResponse.getBody().getMessage());

        UUID carId = carResponse.getBody().getId();

        BaseCar car = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals("Polonez", car.getBrand());
        Assertions.assertFalse(car.getHasActiveSale());
        Assertions.assertEquals(BaseCar.CarStatus.CAR_READY, car.getStatus());
    }

    @Test
    public void createCarAsNonPrivilegedUserTest(){
        final int carCountBefore = baseCarRepository.findAll().size();

        final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";
        final String token = getToken("jan@nowak.pl");

        CreateCarRequest createCarRequest = new CreateCarRequest(
                "Polonez",
                "Caro Plus",
                1999,
                300_000F,
                104F,
                140F,
                1.4F,
                8F,
                6.5F,
                300.99F
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");
        createCarHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CreateCarRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

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

        CreateCarRequest createCarRequest = new CreateCarRequest(
                "Polonez",
                "Caro Plus",
                1999,
                300_000F,
                104F,
                140F,
                1.4F,
                8F,
                6.5F,
                300.99F
        );

        HttpHeaders createCarHeaders = new HttpHeaders();
        createCarHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<CreateCarRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

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
                email,
                "Qwerty123!"
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
