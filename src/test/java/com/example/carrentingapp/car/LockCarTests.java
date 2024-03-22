package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.BaseCar;
import com.example.carrentingapp.car.BaseCarRepository;
import com.example.carrentingapp.car.CarLock;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.request.CreateCarRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarLockService;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.service.UserCreateService;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LockCarTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BaseCarRepository baseCarRepository;

    @Autowired
    private CarLockService carLockService;

    @LocalServerPort
    int randomServerPort;

    //BLOKOWANIE

    @Test
    public void lockCarAsAdminTest(){
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(3)
        );

        final String token = getToken("adam@kowalski.pl");

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CarLockRequest> carLockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                lockUrl,
                carLockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), carLockResponse.getStatusCode());
        Assertions.assertNotNull(carLockResponse.getBody());
        Assertions.assertEquals("Car is now unavailable to rent", carLockResponse.getBody().getMessage());

        //sprawdzenie czy napewno samochód został zablokowany

        BaseCar carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(BaseCar.CarStatus.CAR_LOCKED, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNotNull(lock);
        Assertions.assertEquals(CarLock.CarLockReason.TUNING, lock.getReason());
    }

    @Test
    public void lockCarAsUserTest(){
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(3)
        );

        final String token = getToken("jan@nowak.pl");

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CarLockRequest> carLockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                lockUrl,
                carLockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carLockResponse.getStatusCode());

        //sprawdzenie czy samochód nie został zablokowany

        BaseCar carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(BaseCar.CarStatus.CAR_READY, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNull(lock);
    }

    @Test
    public void lockCarNotAuthorizedTest(){
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(3)
        );

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<CarLockRequest> carLockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                lockUrl,
                carLockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carLockResponse.getStatusCode());

        //sprawdzenie czy samochód nie został zablokowany

        BaseCar carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(BaseCar.CarStatus.CAR_READY, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNull(lock);
    }

    @Test
    public void lockNonExistingCarTest(){
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = UUID.randomUUID();

        CarLockRequest lockRequest = new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(3)
        );

        final String token = getToken("adam@kowalski.pl");

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<CarLockRequest> carLockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                lockUrl,
                carLockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), carLockResponse.getStatusCode());
    }

    //ODBLOKOWYWANIE

    @Test
    public void unlockCarAsAdminTest(){
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(1)
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(carId);

        final String adminToken = getToken("adam@kowalski.pl");

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<CarUnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                unlockUrl,
                unlockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), carLockResponse.getStatusCode());
        Assertions.assertNotNull(carLockResponse.getBody());
        Assertions.assertEquals("Car is now available for renting again", carLockResponse.getBody().getMessage());
    }

    @Test
    public void unlockCarAsUserTest(){
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(1)
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(carId);

        final String userToken = getToken("jan@nowak.pl");

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + userToken);

        HttpEntity<CarUnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                unlockUrl,
                unlockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carLockResponse.getStatusCode());
    }

    @Test
    public void unlockCarNotAuthorizedTest(){
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                carId,
                CarLock.CarLockReason.TUNING.name(),
                LocalDate.now().plusMonths(1)
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(carId);

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<CarUnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                unlockUrl,
                unlockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), carLockResponse.getStatusCode());
    }

    @Test
    public void unlockNotLockedCarTest(){
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarUnlockRequest unlockRequest = new CarUnlockRequest(carId);

        final String adminToken = getToken("adam@kowalski.pl");

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<CarUnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<CarLockResponse> carLockResponse = testRestTemplate.postForEntity(
                unlockUrl,
                unlockRequestHttpEntity,
                CarLockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), carLockResponse.getStatusCode());
    }

    private List<UUID> createCarsForTesting(int numberOfCars){
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < numberOfCars; i++) {
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
            Assertions.assertNotNull(carResponse.getBody());

            ids.add(carResponse.getBody().getId());
        }

        return ids;
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
