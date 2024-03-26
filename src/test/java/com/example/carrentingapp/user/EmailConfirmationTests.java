package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.RegistrationRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.EmailVerificationResponse;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationToken;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenRepository;
import com.example.carrentingapp.exception.exception.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
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

    @Test
    public void testEmailConfirmation() throws URISyntaxException {
        final String baseURL = "http://localhost:"+randomServerPort+"/api/v1/auth/register";

        URI uri = new URI(baseURL);
        RegistrationRequest request = new RegistrationRequest(
                Optional.of("Adam"),
                Optional.of("Kowalski"),
                Optional.of("adam1234@kowalski.pl"),
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

        //todo: uwzględnić status tokenu
        final ConfirmationToken confirmationToken = confirmationTokenRepository.findByUser(userAfterRegistration).orElseThrow(
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

}

