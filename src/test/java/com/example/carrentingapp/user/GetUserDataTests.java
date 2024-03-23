package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.response.UserGetDataResponse;
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

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class GetUserDataTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserBaseRepository baseUserRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void getOwnDataAsAdmin(){
        final String token = getToken("adam@kowalski.pl");
        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get";

        UserBase loggedUser = baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertInstanceOf(UserBase.class, response.getBody().getUser());
        Assertions.assertEquals(loggedUser, response.getBody().getUser());
    }

    @Test
    public void getOwnDataAsUser(){
        final String token = getToken("jan@nowak.pl");
        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get";

        UserBase loggedUser = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertInstanceOf(UserBase.class, response.getBody().getUser());
        Assertions.assertEquals(loggedUser, response.getBody().getUser());
    }

    @Test
    public void getOwnDataNotAuthorized(){
        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get";

        UserBase loggedUser = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void getSpecifiedUserDataAsAdmin(){
        final String token = getToken("adam@kowalski.pl");

        UserBase user = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow();

        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get/id?id=" + user.getId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertInstanceOf(UserBase.class, response.getBody().getUser());
        Assertions.assertEquals(user, response.getBody().getUser());
    }

    @Test
    public void getSpecifiedUserDataAsUser(){
        final String token = getToken("jan@nowak.pl");

        UserBase user = baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow();

        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get/id?id=" + user.getId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void getSpecifiedUserDataNotAuthorized(){
        UserBase user = baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow();

        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get/id?id=" + user.getId().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void getDataOfNonExistingUser(){
        final String token = getToken("adam@kowalski.pl");

        final String url = "http://localhost:" + randomServerPort + "/api/v1/user/get/id?id=" + UUID.randomUUID();

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<UserGetDataResponse> response = testRestTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                UserGetDataResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
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
