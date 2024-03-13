package com.example.carrentingapp.integration.car;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.CarLockRepository;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.request.CreateCarRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.car.response.MainCarResponse;
import com.example.carrentingapp.car.service.CarLockService;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CarLockByApiTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Autowired
    private BaseCarRepository baseCarRepository;

    @Autowired
    private CarLockRepository carLockRepository;

    @Autowired
    private CarLockService carLockService;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testCarLockAsAdmin() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        URI uri = new URI(url);

        CarLockRequest request = new CarLockRequest(
                carId,
                CarLock.CarReason.TUNING.name(),
                (LocalDate.now()).plusDays(22)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        HttpEntity<CarLockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Car is now unavailable to rent", response.getBody().getMessage());
    }

    @Test
    public void testCarLockAsUser() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        URI uri = new URI(url);

        CarLockRequest request = new CarLockRequest(
                carId,
                CarLock.CarReason.TUNING.name(),
                (LocalDate.now()).plusDays(22)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        HttpEntity<CarLockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void testCarLockAsNotAuthorized() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        URI uri = new URI(url);

        CarLockRequest request = new CarLockRequest(
                carId,
                CarLock.CarReason.TUNING.name(),
                (LocalDate.now()).plusDays(22)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<CarLockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void testCarUnlockAsAdmin() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        carLockService.lockCar(
                new CarLockRequest(
                        carId,
                        CarLock.CarReason.TUNING.name(),
                        (LocalDate.now()).plusDays(22)
                )
        );

        URI uri = new URI(url);

        CarUnlockRequest request = new CarUnlockRequest(carId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        HttpEntity<CarUnlockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("Car is now available for renting again", response.getBody().getMessage());
    }

    @Test
    public void testCarUnlockAsUser() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        carLockService.lockCar(
                new CarLockRequest(
                        carId,
                        CarLock.CarReason.TUNING.name(),
                        (LocalDate.now()).plusDays(22)
                )
        );

        URI uri = new URI(url);

        CarUnlockRequest request = new CarUnlockRequest(carId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("jan@nowak.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        HttpEntity<CarUnlockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void testCarUnlockAsNotAuthorized() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = commonFunctionsProvider.createCarForTest();

        carLockService.lockCar(
                new CarLockRequest(
                        carId,
                        CarLock.CarReason.TUNING.name(),
                        (LocalDate.now()).plusDays(22)
                )
        );

        URI uri = new URI(url);

        CarUnlockRequest request = new CarUnlockRequest(carId);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<CarUnlockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarLockResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }
}
