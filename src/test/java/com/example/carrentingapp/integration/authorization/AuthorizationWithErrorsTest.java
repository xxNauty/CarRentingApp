package com.example.carrentingapp.integration.authorization;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.BaseUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.Test;
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
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationWithErrorsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BaseUserRepository repository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void invalidCredentials() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                "jan@kowalski.pl",
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

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void registrationWithIncorrectEmail() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = repository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam",
                "Kowalski",
                "adam123kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = repository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationWithIncorrectData() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = repository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam!@#",
                "Kowalski123",
                "adam123@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = repository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationWithNotStrongEnoughPassword() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = repository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam",
                "Kowalski",
                "adam123@kowalski.pl",
                "qwerty",
                LocalDate.now()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = repository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }
}
//todo: weryfikacja wieku, minimum 18 lat do założenia konta