package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.request.CarUpdateDataRequest;
import com.example.carrentingapp.car.request.CarUpdateMileageRequest;
import com.example.carrentingapp.car.response.CarResponse;
import com.example.carrentingapp.exception.exceptions.http_error_404.CarNotFoundException;
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
public class UpdateCarDataTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarBaseRepository baseCarRepository;

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

    //AKTUALIZACJA PRZEBIEGU

    @Test
    public void testUpdateCarMileageAsAdmin() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("adam@kowalski.pl");

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                Optional.of(carId.toString()),
                Optional.of(mileageToAdd)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertTrue(response.getBody().getMessage().contains("Car mileage updated, actual mileage: "));

        float mileageAfterUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();

        Assertions.assertEquals(mileageAfterUpdate, mileageBeforeUpdate + mileageToAdd);
    }

    @Test
    public void testUpdateCarMileageAsUser() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("jan@nowak.pl");

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                Optional.of(carId.toString()),
                Optional.of(mileageToAdd)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());

        float mileageAfterUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();

        Assertions.assertEquals(mileageAfterUpdate, mileageBeforeUpdate);
    }

    @Test
    public void testUpdateCarMileageNotAuthorized() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                Optional.of(carId.toString()),
                Optional.of(mileageToAdd)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());

        float mileageAfterUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();

        Assertions.assertEquals(mileageAfterUpdate, mileageBeforeUpdate);
    }

    @Test
    public void testUpdateNotExistingCarMileage() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = UUID.randomUUID();
        final String token = getToken("adam@kowalski.pl");

        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                Optional.of(carId.toString()),
                Optional.of(mileageToAdd)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    public void testUpdateCarMileageWithNullValues() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final String token = getToken("adam@kowalski.pl");

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                null,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    }

    @Test
    public void testUpdateCarMileageWithEmptyValues() {
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final String token = getToken("adam@kowalski.pl");


        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateMileageRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateMileageUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    }

    //AKTUALIZACJA DANYCH

    @Test
    public void updateCarDataAsAdmin(){
        final String updateDataUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/data";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("adam@kowalski.pl");

        final CarBase carBeforeUpdate = baseCarRepository.findById(carId).orElseThrow();

        CarUpdateDataRequest request = new CarUpdateDataRequest(
                Optional.ofNullable(carBeforeUpdate.getId().toString()),
                Optional.ofNullable(carBeforeUpdate.getBrand()),
                Optional.of("ModelModel"),
                Optional.ofNullable(carBeforeUpdate.getYearOfProduction()),
                Optional.of(300F),
                Optional.ofNullable(carBeforeUpdate.getTorque()),
                Optional.ofNullable(carBeforeUpdate.getEngineSize()),
                Optional.ofNullable(carBeforeUpdate.getAverageFuelConsumption()),
                Optional.ofNullable(carBeforeUpdate.getMinRankOfUser()),
                Optional.ofNullable(carBeforeUpdate.getPricePerDay())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateDataRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateDataUrl,
                httpEntity,
                CarResponse.class
        );

        final CarBase carAfterUpdate = baseCarRepository.findById(carId).orElseThrow();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());

        System.out.println(carBeforeUpdate);
        System.out.println(carAfterUpdate);

        Assertions.assertEquals(carBeforeUpdate.getId(), carAfterUpdate.getId());
        Assertions.assertNotEquals(carBeforeUpdate.getModel(), carAfterUpdate.getModel());
        Assertions.assertNotEquals(carBeforeUpdate.getPower(), carAfterUpdate.getPower());
    }

    @Test
    public void updateCarDataAsUser(){
        final String updateDataUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/data";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("jan@nowak.pl");

        final CarBase carBeforeUpdate = baseCarRepository.findById(carId).orElseThrow();

        CarUpdateDataRequest request = new CarUpdateDataRequest(
                Optional.ofNullable(carBeforeUpdate.getId().toString()),
                Optional.ofNullable(carBeforeUpdate.getBrand()),
                Optional.of("ModelModel"),
                Optional.ofNullable(carBeforeUpdate.getYearOfProduction()),
                Optional.of(300F),
                Optional.ofNullable(carBeforeUpdate.getTorque()),
                Optional.ofNullable(carBeforeUpdate.getEngineSize()),
                Optional.ofNullable(carBeforeUpdate.getAverageFuelConsumption()),
                Optional.ofNullable(carBeforeUpdate.getMinRankOfUser()),
                Optional.ofNullable(carBeforeUpdate.getPricePerDay())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateDataRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateDataUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void updateCarDataNotAuthorized(){
        final String updateDataUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/data";
        final UUID carId = createCarsForTesting(1).get(0);

        final CarBase carBeforeUpdate = baseCarRepository.findById(carId).orElseThrow();

        CarUpdateDataRequest request = new CarUpdateDataRequest(
                Optional.ofNullable(carBeforeUpdate.getId().toString()),
                Optional.ofNullable(carBeforeUpdate.getBrand()),
                Optional.of("ModelModel"),
                Optional.ofNullable(carBeforeUpdate.getYearOfProduction()),
                Optional.of(300F),
                Optional.ofNullable(carBeforeUpdate.getTorque()),
                Optional.ofNullable(carBeforeUpdate.getEngineSize()),
                Optional.ofNullable(carBeforeUpdate.getAverageFuelConsumption()),
                Optional.ofNullable(carBeforeUpdate.getMinRankOfUser()),
                Optional.ofNullable(carBeforeUpdate.getPricePerDay())
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<CarUpdateDataRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateDataUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void updateCarDataWithNullValues(){
        final String updateDataUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/data";
        final String token = getToken("adam@kowalski.pl");

        CarUpdateDataRequest request = new CarUpdateDataRequest(
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateDataRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateDataUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
    }

    @Test
    public void updateCarDataWithEmptyValues(){
        final String updateDataUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/data";
        final String token = getToken("adam@kowalski.pl");

        CarUpdateDataRequest request = new CarUpdateDataRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<CarUpdateDataRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<CarResponse> response = testRestTemplate.postForEntity(
                updateDataUrl,
                httpEntity,
                CarResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
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
