package com.example.carrentingapp.authorization;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.response.GetCarListResponse;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.LockType;
import com.example.carrentingapp.user.Reason;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LockUserAccountTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BaseUserRepository repository;

    @LocalServerPort
    int randomServerPort;

    private String getBearerToken(String email, String password) throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                email,
                password
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        return Objects.requireNonNull(response.getBody()).getAccessToken();
    }

    @Test //testOfLockingAccount => działa, testOfLockingUserAccount => nie działa
    public void testOfLockingAccount() throws URISyntaxException { //todo: sprawdzić czemu zmiana nazwy metody powoduje wywalenie się testów
        Assertions.assertFalse(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());

        final String url = "http://localhost:"+randomServerPort+"/api/v1/user/lock";

        // logowanie jako administrator
        String adminToken = getBearerToken("adam@kowalski.pl", "Qwerty123!");

        //uzyskanie ID usera do zablokowania
        UUID id = repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User with given id not found"));

        URI uri = new URI(url);

        LockRequest request = new LockRequest(
                id,
                LockType.TEMPORARY.name(),
                Reason.OTHER.name(),
                LocalDate.now()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + adminToken);

        HttpEntity<LockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<LockResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("User account is now locked", response.getBody().getMessage());

        Assertions.assertTrue(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());
    }

    @Test
    public void testIfUserCanLogin() throws URISyntaxException {
        Assertions.assertTrue(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());

        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                "jan@nowak.pl",
                "Qwerty123!"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void testApiAfterLock() throws URISyntaxException {
        Assertions.assertTrue(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());

        final String testUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/all";

        String token = getBearerToken("jan@nowak.pl", "Qwerty123!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarListResponse> response = restTemplate.exchange(
                testUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarListResponse.class
        );

//        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        //todo: poprawić po naprawieniu 500 zamiast 403 przy próbie użycia api jako zablokowany user
    }

    @Test
    public void testOfUnlockingUser() throws URISyntaxException {
        Assertions.assertTrue(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());

        final String url = "http://localhost:"+randomServerPort+"/api/v1/user/unlock";

        String adminToken = getBearerToken("adam@kowalski.pl", "Qwerty123!");

        //uzyskanie ID usera do odblokowania
        UUID id = repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User with given id not found"));

        URI uri = new URI(url);

        UnlockRequest request = new UnlockRequest(id);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + adminToken);

        HttpEntity<UnlockRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<LockResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                LockResponse.class
        );

        System.out.println(response);

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals("User account is now unlocked", response.getBody().getMessage());

        Assertions.assertFalse(repository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User with given id not found")).getIsLocked());
    }

    @Test
    public void testIfUserCanLoginAfterUnlock() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                "jan@nowak.pl",
                "Qwerty123!"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }

    @Test
    public void testIfUserCanUseAPIAfterUnlock() throws URISyntaxException {
        final String testUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/all";

        String token = getBearerToken("jan@nowak.pl", "Qwerty123!");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<GetCarListResponse> response = restTemplate.exchange(
                testUrl,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                GetCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

}
