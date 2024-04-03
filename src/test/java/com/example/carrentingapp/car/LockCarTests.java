package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.request.CarLockRequest;
import com.example.carrentingapp.car.request.CarUnlockRequest;
import com.example.carrentingapp.car.response.CarLockResponse;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.car.service.CarLockService;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import org.junit.Before;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class LockCarTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarBaseRepository baseCarRepository;

    @Autowired
    private CarLockService carLockService;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static boolean usersCreated = false;

    @Before
    public void createUsers() {
        if(!usersCreated){
            UserBase admin = new UserBase(
                    "Adam",
                    "Kowalski",
                    "adam@kowalski.pl",
                    passwordEncoder.encode("Qwerty123!"),
                    LocalDate.now().minusYears(18)
            );
            admin.setStatus(UserBase.UserStatus.USER_READY);
            admin.setRole(UserBase.Role.ADMIN);
            userBaseRepository.save(admin);

            UserBase user = new UserBase(
                    "Jan",
                    "Nowak",
                    "jan@nowak.pl",
                    passwordEncoder.encode("Qwerty123!"),
                    LocalDate.now().minusYears(18)
            );
            user.setStatus(UserBase.UserStatus.USER_READY);
            userBaseRepository.save(user);

            usersCreated = true;
        }
    }

    //BLOKOWANIE

    @Test
    public void testLockCarAsAdmin() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
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

        CarBase carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(CarBase.CarStatus.CAR_LOCKED, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNotNull(lock);
        Assertions.assertEquals(CarLock.CarLockReason.TUNING, lock.getReason());
    }

    @Test
    public void testLockCarAsUser() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
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

        CarBase carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(CarBase.CarStatus.CAR_READY, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNull(lock);
    }

    @Test
    public void testLockCarNotAuthorized() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
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

        CarBase carAfterLock = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(CarBase.CarStatus.CAR_READY, carAfterLock.getStatus());

        CarLock lock = carAfterLock.getActiveLock();

        Assertions.assertNull(lock);
    }

    @Test
    public void testLockNonExistingCar() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = UUID.randomUUID();

        CarLockRequest lockRequest = new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
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

    @Test
    public void testLockCarWithNullValues() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                null,
                null,
                null
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

        Assertions.assertEquals(HttpStatusCode.valueOf(500), carLockResponse.getStatusCode());
    }

    @Test
    public void testLockCarWithEmptyValues() {
        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/lock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarLockRequest lockRequest = new CarLockRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
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

        Assertions.assertEquals(HttpStatusCode.valueOf(500), carLockResponse.getStatusCode());
    }

    //ODBLOKOWYWANIE

    @Test
    public void testUnlockCarAsAdmin() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(Optional.ofNullable(carId.toString()));

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
    public void testUnlockCarAsUser() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(Optional.ofNullable(carId.toString()));

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
    public void testUnlockCarNotAuthorized() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(Optional.ofNullable(carId.toString()));

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
    public void testUnlockNotLockedCar() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        CarUnlockRequest unlockRequest = new CarUnlockRequest(Optional.ofNullable(carId.toString()));

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

        Assertions.assertEquals(HttpStatusCode.valueOf(409), carLockResponse.getStatusCode());
    }

    @Test
    public void testUnlockCarWithNullValues() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(null);

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

    @Test
    public void testUnlockCarWithEmptyValues() {
        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/car/unlock";
        final UUID carId = createCarsForTesting(1).get(0);

        //blokowanie samochodu do odblokowania

        carLockService.lockCar(new CarLockRequest(
                Optional.of(carId.toString()),
                Optional.of(CarLock.CarLockReason.TUNING.name()),
                Optional.of(LocalDate.now().plusMonths(3))
        ));

        //odblokowywanie

        CarUnlockRequest unlockRequest = new CarUnlockRequest(Optional.empty());

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

    //==================================================================================================================

    private List<UUID> createCarsForTesting(int numberOfCars) {
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

    private String getToken(String email) {
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
