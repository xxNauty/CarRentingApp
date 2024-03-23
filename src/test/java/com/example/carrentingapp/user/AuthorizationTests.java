package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
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
import java.net.URISyntaxException;
import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthorizationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserBaseRepository baseUserRepository;

    @LocalServerPort
    int randomServerPort;

    //POPRAWNE

    @Test
    public void testRegisterEndpoint() throws Exception {

        final int userCountBefore = baseUserRepository.findAll().size();

        final String url = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(url);

        RegistrationRequest request = new RegistrationRequest(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                "Qwerty123!",
                LocalDate.now().minusYears(20)
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
    public void testLoginEndpoint() throws Exception{
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

    //BŁĘDNE

    @Test
    public void loginWithEmailNotRegisteredTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                "jan@kowalski.pl",
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

        Assertions.assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
    }

    @Test
    public void loginWithIncorrectPasswordAndRegisteredEmailTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                "jan@nowak.pl",
                "Qwerty123!!"
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), response.getStatusCode());
    }

    @Test
    public void registrationWithIncorrectEmailTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam",
                "Kowalski",
                "adam123kowalski.pl",
                "Qwerty123!",
                LocalDate.now().minusYears(20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationWithIncorrectFirstNameTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam!@#",
                "Kowalski",
                "adam123@kowalski.pl",
                "Qwerty123!",
                LocalDate.now().minusYears(20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationWithIncorrectLastNameTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam!@#",
                "Kowalski",
                "adam123@kowalski.pl",
                "Qwerty123!",
                LocalDate.now().minusYears(20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationWithNotStrongEnoughPasswordTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam",
                "Kowalski",
                "adam123@kowalski.pl",
                "qwerty",
                LocalDate.now().minusYears(20)
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

    @Test
    public void registrationAsTooYoungTest() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                "Adam",
                "Kowalski",
                "adam123@kowalski.pl",
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

        int userCountAfter = baseUserRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        Assertions.assertEquals(userCountAfter, userCountBefore);
    }

}
