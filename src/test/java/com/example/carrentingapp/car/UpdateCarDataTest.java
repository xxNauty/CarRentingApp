package com.example.carrentingapp.car;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.request.CarCreateRequest;
import com.example.carrentingapp.car.request.CarUpdateMileageRequest;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UpdateCarDataTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CarBaseRepository baseCarRepository;

    @LocalServerPort
    int randomServerPort;

    //aktualizacja przebiegu

    @Test
    public void updateCarMileageAsAdminTest(){
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("adam@kowalski.pl");

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                carId,
                mileageToAdd
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
    public void updateCarMileageAsUserTest(){
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);
        final String token = getToken("jan@nowak.pl");

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                carId,
                mileageToAdd
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
    public void updateCarMileageNotAuthorizedTest(){
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = createCarsForTesting(1).get(0);

        final float mileageBeforeUpdate = baseCarRepository.findById(carId).orElseThrow().getMileage();
        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                carId,
                mileageToAdd
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
    public void updateNotExistingCarMileageTest(){
        final String updateMileageUrl = "http://localhost:" + randomServerPort + "/api/v1/car/update/mileage";
        final UUID carId = UUID.randomUUID();
        final String token = getToken("adam@kowalski.pl");

        final float mileageToAdd = 123.45F;

        CarUpdateMileageRequest request = new CarUpdateMileageRequest(
                carId,
                mileageToAdd
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


    private List<UUID> createCarsForTesting(int numberOfCars){
        List<UUID> ids = new ArrayList<>();
        for (int i = 0; i < numberOfCars; i++) {
            final String createCarUrl = "http://localhost:" + randomServerPort + "/api/v1/car/create/base";
            final String token = getToken("adam@kowalski.pl");

            CarCreateRequest createCarRequest = new CarCreateRequest(
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

            HttpEntity<CarCreateRequest> createCarRequestHttpEntity = new HttpEntity<>(createCarRequest, createCarHeaders);

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
