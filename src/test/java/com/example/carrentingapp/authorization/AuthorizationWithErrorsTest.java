package com.example.carrentingapp.authorization;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.UserRepository;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthorizationWithErrorsTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository repository;

    @LocalServerPort
    int randomServerPort;

    @Test //todo: dorobić jak naprawiona zostanie obsługa kodów błędów HTTP
    public void invalidCredentials() throws URISyntaxException {
//        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/login";
//
//        URI uri = new URI(baseURL);
//
//        LoginRequest request = new LoginRequest(
//                "adam@kowalski.pl",
//                "Qwerty123"
//        );
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("X-COM-PERSIST", "true");
//
//        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);
//
//        ResponseEntity<AuthenticationResponse> response = restTemplate.postForEntity(
//                uri,
//                httpEntity,
//                AuthenticationResponse.class
//        );
//
//        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}
