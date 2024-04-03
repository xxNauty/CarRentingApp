package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.token.TokenRepository;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class AuthorizationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserBaseRepository baseUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @LocalServerPort
    int randomServerPort;

    //POPRAWNE

    @Test
    public void testRegisterEndpoint() throws Exception {
        final int userCountBefore = baseUserRepository.findAll().size();

        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(url);

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Jan"),
                Optional.of("Kowalski"),
                Optional.of("jan123@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(18))
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

        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getAccessToken());
        Assertions.assertNotNull(response.getBody().getRefreshToken());
        Assertions.assertNotNull(tokenRepository.findByToken(response.getBody().getAccessToken()));
    }

    @Test
    public void testLoginEndpoint() throws Exception {
        //tworzenie użytkownika do testu

        UserBase user = new UserBase(
                "Jan",
                "Kowalski",
                "jan@kowalski.pl",
                passwordEncoder.encode("Qwerty123!"),
                LocalDate.now().minusYears(18)
        );
        user.setStatus(UserBase.UserStatus.USER_READY);
        baseUserRepository.save(user);

        //docelowy test

        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(url);

        LoginRequest request = new LoginRequest(
                Optional.of("jan@kowalski.pl"),
                Optional.of("Qwerty123!")
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

        Assertions.assertNotNull(response.getBody());
        Assertions.assertNotNull(response.getBody().getAccessToken());
        Assertions.assertNotNull(response.getBody().getRefreshToken());
        Assertions.assertNotNull(tokenRepository.findByToken(response.getBody().getAccessToken()));
    }

    //TEST DLA WARTOŚCI NULL

    @Test
    public void testLoginWithNullValues() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(url);

        LoginRequest request = new LoginRequest(null, null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegisterWithNullValues() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(url);

        RegistrationRequest request = new RegistrationRequest(null, null, null,
                null, null);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    //TEST DLA PUSTYCH WARTOŚCI

    @Test
    public void testLoginWithEmptyValues() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(url);

        LoginRequest request = new LoginRequest(Optional.empty(), Optional.empty());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegisterWithEmptyValues() throws URISyntaxException {
        final String url = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(url);

        RegistrationRequest request = new RegistrationRequest(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty());

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    //BŁĘDNE

    @Test
    public void testLoginWithEmailNotRegistered() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                Optional.of("jan@kowalski.pl"),
                Optional.of("Qwerty123!")
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testLoginWithIncorrectPasswordAndRegisteredEmail() throws URISyntaxException {
        //tworzenie użytkownika do testu

        UserBase user = new UserBase(
                "Jan",
                "Nowak",
                "jan@nowak.pl",
                passwordEncoder.encode("Qwerty123!"),
                LocalDate.now().minusYears(18)
        );
        user.setStatus(UserBase.UserStatus.USER_READY);
        baseUserRepository.save(user);

        //docelowy test

        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI uri = new URI(baseURL);

        LoginRequest request = new LoginRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Qwerty123!!")
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegistrationWithIncorrectEmailTest() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam123kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(18))
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegistrationWithIncorrectFirstName() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam!@#"),
                Optional.of("Kowalski"),
                Optional.of("adam123@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(18))
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegistrationWithIncorrectLastName() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski!!@"),
                Optional.of("adam123@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(18))
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegistrationWithNotStrongEnoughPassword() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam123@kowalski.pl"),
                Optional.of("qwerty"),
                Optional.of(LocalDate.now().minusYears(18))
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

    @Test
    public void testRegistrationAsTooYoung() throws URISyntaxException {
        final String baseURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(baseURL);

        int userCountBefore = baseUserRepository.findAll().size();

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam123@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(18).plusDays(1))
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

        Assertions.assertNotNull(response.getBody());

        Assertions.assertNull(response.getBody().getAccessToken());
        Assertions.assertNull(response.getBody().getRefreshToken());
    }

}
