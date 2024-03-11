package com.example.carrentingapp.integration.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CreateCarRequest;
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
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateCarTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommonFunctionsProvider provider;

    @LocalServerPort
    int randomServerPort;


    @Test
    public void correctCreateCar() throws URISyntaxException {
        final String token = provider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, restTemplate);
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";

        URI uri = new URI(baseURL);

        CreateCarRequest request = new CreateCarRequest(
                "Ford",
                "Focus",
                2024,
                0.0F,
                300F,
                400F,
                2.0F,
                7.3F,
                5.5F,
                340F
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CreateCarRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().getMessage().contains("Car created"));
    }

    @Test
    public void createCarNotByAdmin() throws URISyntaxException {
        final String token = provider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, restTemplate);
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";

        URI uri = new URI(baseURL);

        CreateCarRequest request = new CreateCarRequest(
                "Ford",
                "Focus",
                2024,
                0.0F,
                300F,
                400F,
                2.0F,
                7.3F,
                5.5F,
                340F
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CreateCarRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void createCarNotAuthorized() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";

        URI uri = new URI(baseURL);

        CreateCarRequest request = new CreateCarRequest(
                "Ford",
                "Focus",
                2024,
                0.0F,
                300F,
                400F,
                2.0F,
                7.3F,
                5.5F,
                340F
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<CreateCarRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

}
