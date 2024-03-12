package com.example.carrentingapp.integration.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.response.GetCarListResponse;
import com.example.carrentingapp.car.response.GetCarResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URISyntaxException;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetCarFromApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CommonFunctionsProvider provider;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testGetSingleCarAsUser() throws URISyntaxException {
        final UUID id = provider.createCarForTest();
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/" + id;

        String token = provider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getCars().getId(), id);
    }

    @Test
    public void testGetSingleCarAsAdmin() throws URISyntaxException {
        final UUID id = provider.createCarForTest();
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/" + id;

        String token = provider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(response.getBody().getCars().getId(), id);
    }

    @Test
    public void testGetSingleCarNotAuthorized() throws URISyntaxException {
        final UUID id = provider.createCarForTest();
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/" + id;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<GetCarResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void testGetListOfCarsAsUser() throws URISyntaxException {
        for (int i = 0; i < 5; i++) {
            provider.createCarForTest();
        }

        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/all";

        String token = provider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
//        Assertions.assertEquals(5 + 3, response.getBody().getCars().size()); //+3 bo z dwóch poprzednich testów zostają
        Assertions.assertInstanceOf(BaseCar.class, response.getBody().getCars().get(0));
    }

    @Test
    public void testGetListOfCarsAsAdmin() throws URISyntaxException {

        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/all";

        String token = provider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, restTemplate);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
//        Assertions.assertEquals(8, response.getBody().getCars().size());
        Assertions.assertInstanceOf(BaseCar.class, response.getBody().getCars().get(0));
    }

    @Test
    public void testGetListOfCarsNotAuthorized(){
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/get/all";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<GetCarListResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

}
