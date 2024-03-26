package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.response.CarGetResponse;
import com.example.carrentingapp.car.response.CarGetSimpleListResponse;
import com.example.carrentingapp.car.service.CarLockService;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GetCarDataTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarBaseRepository baseCarRepository;

    @Autowired
    private CarLockRepository carLockRepository;

    @Autowired
    private CarLockService carLockService;

    @LocalServerPort
    int randomServerPort;

    //pobieranie danych samochodu po jego ID

    @After //wyraźnie szybsze niż @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    public void clearDatabase(){
        carLockRepository.deleteAll();
        baseCarRepository.deleteAll();
    }

    @Test
    public void getSpecifiedCarAuthorizedTest(){
        final UUID carId = createCarsForTesting(1).get(0);
        final String getCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get?id=" + carId;

        //jako admin

        final String adminToken = getToken("adam@kowalski.pl");

        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.set("X-COM-PERSIST", "true");
        adminHeaders.set("Authorization", "Bearer " + adminToken);

        ResponseEntity<CarGetResponse> adminResponse = testRestTemplate.exchange(
                getCarUrl,
                HttpMethod.GET,
                new HttpEntity<>(adminHeaders),
                CarGetResponse.class
        );

        CarBase carFromDatabase = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), adminResponse.getStatusCode());
        Assertions.assertNotNull(adminResponse.getBody());
        Assertions.assertEquals(carFromDatabase, adminResponse.getBody().getCar());

        //jako zwykły user

        final String userToken = getToken("jan@nowak.pl");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("X-COM-PERSIST", "true");
        userHeaders.set("Authorization", "Bearer " + userToken);

        ResponseEntity<CarGetResponse> userResponse = testRestTemplate.exchange(
                getCarUrl,
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                CarGetResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), userResponse.getStatusCode());
        Assertions.assertNotNull(userResponse.getBody());
        Assertions.assertEquals(carFromDatabase, userResponse.getBody().getCar());
    }

    @Test
    public void getSpecifiedCarNotAuthorizedTest(){
        final UUID carId = createCarsForTesting(1).get(0);
        final String getCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get?id=" + carId;

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<CarGetResponse> response = testRestTemplate.exchange(
                getCarUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    //pobranie uproszczonej listy wszystkich samochodów

    @Test
    public void getSimplifiedListOfAllCarsTest(){
        createCarsForTesting(5);
        final String getSimplifiedCarListUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/simple/all";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<CarGetSimpleListResponse> response = testRestTemplate.exchange(
                getSimplifiedCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(5, response.getBody().getCars().size());
    }

    //pobieranie uproszczonej listy dostępnych samochodów

    @Test
    public void getSimplifiedListOfAvailableCarsAuthorizedTest(){
        //jako admin

        List<UUID> carIds = createCarsForTesting(5);
        carLockService.lockCar(new CarLockRequest(
                Optional.of(carIds.get(3).toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(2))
        ));
        carLockService.lockCar(new CarLockRequest(
                Optional.of(carIds.get(1).toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(2))
        ));

        final String getSimplifiedCarListUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/simple/available";

        final String adminToken = getToken("adam@kowalski.pl");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + adminToken);



        ResponseEntity<CarGetSimpleListResponse> response = testRestTemplate.exchange(
                getSimplifiedCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(3, response.getBody().getCars().size());

        //jako normalny user

        final String userToken = getToken("adam@kowalski.pl");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("X-COM-PERSIST", "true");
        userHeaders.set("Authorization", "Bearer " + userToken);

        ResponseEntity<CarGetSimpleListResponse> userResponse = testRestTemplate.exchange(
                getSimplifiedCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), userResponse.getStatusCode());
        Assertions.assertNotNull(userResponse.getBody());
        Assertions.assertEquals(3, userResponse.getBody().getCars().size());
    }

    @Test
    public void getSimplifiedListOfAvailableCarsNotAuthorizedTest(){
        createCarsForTesting(5);
        final String getSimplifiedCarListUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/simple/available";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<CarGetSimpleListResponse> response = testRestTemplate.exchange(
                getSimplifiedCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    //pobieranie pełnej listy dostępnych samochodów

    @Test
    public void getFullListOfAvailableCarsAuthorizedTest(){
        //jako admin

        List<UUID> carIds = createCarsForTesting(5);
        carLockService.lockCar(new CarLockRequest(
                Optional.of(carIds.get(3).toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(2))
        ));
        carLockService.lockCar(new CarLockRequest(
                Optional.of(carIds.get(1).toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(2))
        ));

        final String getFullCarListUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/full/available";

        final String adminToken = getToken("adam@kowalski.pl");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + adminToken);

        ResponseEntity<CarGetSimpleListResponse> response = testRestTemplate.exchange(
                getFullCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(3, response.getBody().getCars().size());

        //jako normalny user

        final String userToken = getToken("adam@kowalski.pl");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.set("X-COM-PERSIST", "true");
        userHeaders.set("Authorization", "Bearer " + userToken);

        ResponseEntity<CarGetSimpleListResponse> userResponse = testRestTemplate.exchange(
                getFullCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(userHeaders),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), userResponse.getStatusCode());
        Assertions.assertNotNull(userResponse.getBody());
        Assertions.assertEquals(3, userResponse.getBody().getCars().size());
    }

    @Test
    public void getFullListOfAvailableCarsNotAuthorizedTest(){
        createCarsForTesting(5);
        final String getFullCarListUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/full/available";

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<CarGetSimpleListResponse> response = testRestTemplate.exchange(
                getFullCarListUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                CarGetSimpleListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    private List<UUID> createCarsForTesting(int numberOfCars){
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < numberOfCars; i++) {
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
            Assertions.assertNotNull(carResponse.getBody());

            ids.add(UUID.fromString(carResponse.getBody().getId()));
        }

        return ids;
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
