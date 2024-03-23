package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.ForgotPasswordVerificationRequest;
import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.request.NewPasswordRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.authentication.response.ForgotPasswordResponse;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationToken;
import com.example.carrentingapp.email.notifications.forgot_password.token.ForgotPasswordVerificationTokenRepository;
import com.example.carrentingapp.exception.exception.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
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

import java.util.UUID;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class ForgotPasswordTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserBaseRepository baseUserRepository;

    @Autowired
    private ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void correctForgotPasswordTest() throws Exception{
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@kowalski.pl");

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
                "adam@kowalski.pl",
                verificationToken.getToken(),
                "Qwerty123321!!"
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
                "adam@kowalski.pl",
                "Qwerty123321!!"
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

    @Test
    public void emailNotExistInDatabaseForgotPasswordTest(){
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@nowak.pl");

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

    public void incorrectTokenForgotPasswordTest(){
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@kowalski.pl");

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

        //ustawianie nowego hasła przy użyciu błędnego tokenu

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                "adam@kowalski.pl",
                UUID.randomUUID().toString(),
                "Qwerty123321!!"
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
    public void newPasswordDoesNotMatchMinimumRequirementForgotPasswordTest(){
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@kowalski.pl");

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
                "adam@kowalski.pl",
                verificationToken.getToken(),
                "Qwerty"
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
    public void incorrectEmailUsedInSettingNewPasswordForgotPasswordTest(){
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //poprawne wysłanie maila z kodem weryfikacyjnym

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@kowalski.pl");

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

        //błędny adres email w formularzu ustawiania nowego hasła

        NewPasswordRequest newPasswordRequest = new NewPasswordRequest(
                "adam@kowal.pl",
                verificationToken.getToken(),
                "Qwerty123!!@"
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
