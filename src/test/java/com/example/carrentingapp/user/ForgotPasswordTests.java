package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.ForgotPasswordVerificationRequest;
import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.NewPasswordRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.ForgotPasswordResponse;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationToken;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationTokenRepository;
import com.example.carrentingapp.exception.exceptions.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exceptions.http_error_404.UserNotFoundException;
import org.junit.Before;
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

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ForgotPasswordTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;

    @LocalServerPort
    int randomServerPort;

    //przygotowanie danych do testów

    @Autowired
    private UserBaseRepository baseUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Before
    public void createUsers() {
        UserBase admin = new UserBase(
                "Adam",
                "Kowalski",
                "adam@kowalski.pl",
                passwordEncoder.encode("Qwerty123!"),
                LocalDate.now().minusYears(18)
        );
        admin.setStatus(UserBase.UserStatus.USER_READY);
        admin.setRole(UserBase.Role.ADMIN);
        baseUserRepository.save(admin);

        UserBase user = new UserBase(
                "Jan",
                "Nowak",
                "jan@nowak.pl",
                passwordEncoder.encode("Qwerty123!"),
                LocalDate.now().minusYears(18)
        );
        user.setStatus(UserBase.UserStatus.USER_READY);
        baseUserRepository.save(user);
    }

    //POPRAWNE

    @Test
    public void testCorrectForgotPassword() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("adam@kowalski.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        ForgotPasswordVerificationToken verificationToken
                = forgotPasswordVerificationTokenRepository.findActiveTokenForUser(
                baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId(),
                ForgotPasswordVerificationToken.ForgotPasswordTokenStatus.CONFIRMATION_TOKEN_SENT
        ).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        //ustawianie nowego hasła

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                Optional.of("adam@kowalski.pl"),
                Optional.of(verificationToken.getToken()),
                Optional.of("Qwerty123321!!")
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), setNewPasswordResponse.getStatusCode());
        Assertions.assertNotNull(setNewPasswordResponse.getBody());
        Assertions.assertEquals("New password set successfully", setNewPasswordResponse.getBody().getMessage());

        //sprawdzenie czy nowe hasło pozwala na zalogowanie się

        LoginRequest loginRequest = new LoginRequest(
                Optional.of("adam@kowalski.pl"),
                Optional.of("Qwerty123321!!")
        );

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> loginRequestHttpEntity = new HttpEntity<>(loginRequest, loginHeaders);

        ResponseEntity<AuthenticationResponse> authenticationResponse = testRestTemplate.postForEntity(
                loginURL,
                loginRequestHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), authenticationResponse.getStatusCode());
        Assertions.assertNotNull(authenticationResponse.getBody());
    }

    //WARTOŚCI NULL

    @Test
    public void testSendTokenRequestWithNullValue() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(null);

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), sendVerificationEmailResponse.getStatusCode());
    }

    @Test
    public void testSetNewPasswordRequestWithNullValues() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("adam@kowalski.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        //ustawianie nowego hasła

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
            null,
            null,
            null
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), setNewPasswordResponse.getStatusCode());
    }

    //PUSTE WARTOŚCI

    @Test
    public void testSendTokenRequestWithEmptyValue() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.empty());

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), sendVerificationEmailResponse.getStatusCode());
    }

    @Test
    public void testSetNewPasswordRequestWithEmptyValues() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("adam@kowalski.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        //ustawianie nowego hasła

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), setNewPasswordResponse.getStatusCode());
    }

    //BŁĘDNE

    @Test
    public void testEmailNotExistInDatabase() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("adam@nowak.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), sendVerificationEmailResponse.getStatusCode());
    }

    public void testIncorrectToken() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("jan@nowak.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        ForgotPasswordVerificationToken verificationToken
                = forgotPasswordVerificationTokenRepository.findActiveTokenForUser(
                baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId(),
                ForgotPasswordVerificationToken.ForgotPasswordTokenStatus.CONFIRMATION_TOKEN_SENT
        ).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        //ustawianie nowego hasła przy użyciu błędnego tokenu

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of(UUID.randomUUID().toString()),
                Optional.of("Qwerty123321!!")
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), setNewPasswordResponse.getStatusCode());
    }

    @Test
    public void testNewPasswordDoesNotMatchMinimumRequirement() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("adam@kowalski.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        ForgotPasswordVerificationToken verificationToken
                = forgotPasswordVerificationTokenRepository.findActiveTokenForUser(
                baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId(),
                ForgotPasswordVerificationToken.ForgotPasswordTokenStatus.CONFIRMATION_TOKEN_SENT
        ).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        //ustawianie zbyt słabego hasła jako nowe

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                Optional.of("adam@kowalski.pl"),
                Optional.of(verificationToken.getToken()),
                Optional.of("Qwerty")
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), setNewPasswordResponse.getStatusCode());
    }

    @Test
    public void testIncorrectEmailUsedInSettingNewPassword() {
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest(Optional.of("jan@nowak.pl"));

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> sendVerificationEmailResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), sendVerificationEmailResponse.getStatusCode());
        Assertions.assertNotNull(sendVerificationEmailResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                sendVerificationEmailResponse.getBody().getMessage()
        );

        ForgotPasswordVerificationToken verificationToken
                = forgotPasswordVerificationTokenRepository.findActiveTokenForUser(
                baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId(),
                ForgotPasswordVerificationToken.ForgotPasswordTokenStatus.CONFIRMATION_TOKEN_SENT
        ).orElseThrow(() -> new TokenNotFoundException("Token not found"));

        //błędny adres email w formularzu ustawiania nowego hasła

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                Optional.of("jan@kowal.pl"),
                Optional.of(verificationToken.getToken()),
                Optional.of("Qwerty123!!@")
        );

        HttpHeaders newPasswordHeaders = new HttpHeaders();
        newPasswordHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<NewPasswordRequest> newPasswordRequestHttpEntity = new HttpEntity<>(newPasswordRequest, newPasswordHeaders);

        ResponseEntity<ForgotPasswordResponse> setNewPasswordResponse = testRestTemplate.postForEntity(
                setNewPasswordURL,
                newPasswordRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), setNewPasswordResponse.getStatusCode());
    }
}
