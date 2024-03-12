package com.example.carrentingapp.integration.authorization;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.BaseUserRepository;
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
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class CorrectAuthorizationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void createUser() throws Exception {

        final int userCountBefore = baseUserRepository.findAll().size();

        final String url = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(url);

        RegistrationRequest request = new RegistrationRequest(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                "Qwerty123!",
                LocalDate.now()
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        final int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(userCountBefore, userCountAfter - 1);
    }

    @Test
    public void login() throws Exception{
        final String url = "http://localhost:"+randomServerPort+"/api/v1/auth/login";

        URI uri = new URI(url);

        LoginRequest request = new LoginRequest(
                "adam@kowalski.pl",
                "Qwerty123!"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());

    }

}
