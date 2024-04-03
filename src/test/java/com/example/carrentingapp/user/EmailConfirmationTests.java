package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.request.SendVerifyingTokenAgainRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.EmailVerificationResponse;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationToken;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenRepository;
import com.example.carrentingapp.exception.exceptions.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exceptions.http_error_404.UserNotFoundException;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class EmailConfirmationTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private UserBaseRepository baseUserRepository;

    @LocalServerPort
    int randomServerPort;

    //POPRAWNE

    @Test
    public void testEmailConfirmation() throws URISyntaxException {
        final String registerUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(registerUrl);
        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(20))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        UserBase userAfterRegistration = baseUserRepository.findByEmail(request.email.get()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(UserBase.UserStatus.USER_CREATED, userAfterRegistration.getStatus());
        Assertions.assertEquals(UserBase.Role.USER, userAfterRegistration.getRole());

        //potwierdzenie adresu email

        final ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(userAfterRegistration.getId()).orElseThrow(
                () -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));

        final String confirmationURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register/verify?token=" + confirmationToken.getToken();

        HttpHeaders confirmationHeaders = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<EmailVerificationResponse> confirmationResponse = testRestTemplate.exchange(
                confirmationURL,
                HttpMethod.GET,
                new HttpEntity<>(confirmationHeaders),
                EmailVerificationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), confirmationResponse.getStatusCode());
        Assertions.assertNotNull(confirmationResponse.getBody());
        Assertions.assertEquals("Email verified, your account is now enabled", confirmationResponse.getBody().getMessage());

        UserBase userAfterEmailConfirmation = baseUserRepository.findByEmail(request.email.get()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(UserBase.UserStatus.USER_READY, userAfterEmailConfirmation.getStatus());
    }

    @Test
    public void testEmailConfirmationTokenResent() throws URISyntaxException {
        final String registerUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI registerUri = new URI(registerUrl);

        RegistrationRequest registrationRequest = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam1@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(20))
        );

        HttpHeaders registerHeaders = new HttpHeaders();
        registerHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> registrationHttpEntity = new HttpEntity<>(registrationRequest, registerHeaders);

        ResponseEntity<AuthenticationResponse> registerResponse = testRestTemplate.postForEntity(
                registerUri,
                registrationHttpEntity,
                AuthenticationResponse.class
        );

        UserBase userAfterRegistration = baseUserRepository.findByEmail(registrationRequest.email.get()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200), registerResponse.getStatusCode());
        Assertions.assertEquals(UserBase.UserStatus.USER_CREATED, userAfterRegistration.getStatus());
        Assertions.assertEquals(UserBase.Role.USER, userAfterRegistration.getRole());

        //pobranie tokenu wygenerowanego automatycznie po rejestracji

        final ConfirmationToken autoGeneratedToken = confirmationTokenRepository.findByUser(userAfterRegistration.getId()).orElseThrow(
                () -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));
        Assertions.assertEquals(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_SENT, autoGeneratedToken.getStatus());

        //wygenerowanie nowego tokenu weryfikacyjnego

        final String newTokenUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register/verify/send-again";

        URI newTokenUri = new URI(newTokenUrl);

        SendVerifyingTokenAgainRequest newTokenRequest = new SendVerifyingTokenAgainRequest(
                Optional.of(userAfterRegistration.getEmail()),
                Optional.of(userAfterRegistration.getId().toString())
        );

        HttpHeaders newTokenHttpHeaders = new HttpHeaders();
        newTokenHttpHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<SendVerifyingTokenAgainRequest> newTokenHttpEntity = new HttpEntity<>(newTokenRequest, newTokenHttpHeaders);

        ResponseEntity<EmailVerificationResponse> newTokenResponse = testRestTemplate.postForEntity(
                newTokenUri,
                newTokenHttpEntity,
                EmailVerificationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), newTokenResponse.getStatusCode());

        //pobranie danych nowego tokenu

        final ConfirmationToken newToken = confirmationTokenRepository.findByUser(userAfterRegistration.getId()).orElseThrow(
                () -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));
        Assertions.assertEquals(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_SENT, newToken.getStatus());

        //sprawdzenie czy stary stracił ważność

        final ConfirmationToken oldToken = confirmationTokenRepository.findById(autoGeneratedToken.getId()).orElseThrow(
                () -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));
        Assertions.assertEquals(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_EXPIRED, oldToken.getStatus());

        //sprawdzenie czy stary token pozwala na weryfikację adresu email

        final String emailVerificationUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register/verify?token=" + oldToken.getToken();

        HttpHeaders emailVerificationHeaders = new HttpHeaders();
        emailVerificationHeaders.set("X-COM-PERSIST", "true");

        ResponseEntity<EmailVerificationResponse> emailVerificationResponse = testRestTemplate.exchange(
                emailVerificationUrl,
                HttpMethod.GET,
                new HttpEntity<>(emailVerificationHeaders),
                EmailVerificationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(409), emailVerificationResponse.getStatusCode());

        ConfirmationToken oldTokenAfterConfirmation = confirmationTokenRepository.findById(oldToken.getId()).orElseThrow();

        Assertions.assertNull(oldTokenAfterConfirmation.getConfirmedAt());
        Assertions.assertEquals(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_EXPIRED, oldTokenAfterConfirmation.getStatus());

        //poprawna weryfikacja nowym tokenem

        final String correctEmailVerificationUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register/verify?token=" + newToken.getToken();

        HttpHeaders correctEmailVerificationHeaders = new HttpHeaders();
        correctEmailVerificationHeaders.set("X-COM-PERSIST", "true");

        ResponseEntity<EmailVerificationResponse> correctEmailVerificationResponse = testRestTemplate.exchange(
                correctEmailVerificationUrl,
                HttpMethod.GET,
                new HttpEntity<>(correctEmailVerificationHeaders),
                EmailVerificationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), correctEmailVerificationResponse.getStatusCode());

        ConfirmationToken newTokenAfterConfirmation = confirmationTokenRepository.findById(newToken.getId()).orElseThrow();

        Assertions.assertNotNull(newTokenAfterConfirmation.getConfirmedAt());
        Assertions.assertEquals(ConfirmationToken.ConfirmationTokenStatus.CONFIRMATION_TOKEN_CONFIRMED, newTokenAfterConfirmation.getStatus());

        UserBase ownerOfToken = newTokenAfterConfirmation.getUser();

        Assertions.assertEquals(UserBase.UserStatus.USER_READY, ownerOfToken.getStatus());
    }

    //BŁĘDNE

    @Test
    public void testEmailConfirmationWithInvalidToken() throws URISyntaxException {
        final String registerUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(registerUrl);
        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam2@kowalski.pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(20))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        UserBase userAfterRegistration = baseUserRepository.findByEmail(request.email.get()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
        Assertions.assertEquals(UserBase.UserStatus.USER_CREATED, userAfterRegistration.getStatus());
        Assertions.assertEquals(UserBase.Role.USER, userAfterRegistration.getRole());

        //próba potwierdzenia adresu email błędnym tokenem

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));

        final String confirmationURL = "http://localhost:" + randomServerPort + "/api/v1/auth/register/verify?token=" + UUID.randomUUID();

        HttpHeaders confirmationHeaders = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        ResponseEntity<EmailVerificationResponse> confirmationResponse = testRestTemplate.exchange(
                confirmationURL,
                HttpMethod.GET,
                new HttpEntity<>(confirmationHeaders),
                EmailVerificationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), confirmationResponse.getStatusCode());

        UserBase userAfterEmailConfirmation = baseUserRepository.findByEmail(request.email.get()).orElseThrow(
                () -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(UserBase.UserStatus.USER_CREATED, userAfterEmailConfirmation.getStatus());
    }

    @Test
    public void testIfTokenIsGeneratedAtIncorrectRegister() throws URISyntaxException {
        final int tokenCountBefore = confirmationTokenRepository.findAll().size();

        final String registerUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/register";

        URI uri = new URI(registerUrl);

        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam2kowalski3pl"),
                Optional.of("Qwerty123!"),
                Optional.of(LocalDate.now().minusYears(20))
        );

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<RegistrationRequest> httpEntity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthenticationResponse> response = testRestTemplate.postForEntity(
                uri,
                httpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());

        UserBase userAfterRegistration = baseUserRepository.findByEmail(request.email.get()).orElse(null);
        Assertions.assertNull(userAfterRegistration);

        final int tokenCountAfter = confirmationTokenRepository.findAll().size();

        Assertions.assertEquals(tokenCountBefore, tokenCountAfter);
    }
}

