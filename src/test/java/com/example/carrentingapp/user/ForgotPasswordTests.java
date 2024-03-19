package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.ForgotPasswordVerificationRequest;
import com.example.carrentingapp.authentication.request.NewPasswordRequest;
import com.example.carrentingapp.authentication.response.ForgotPasswordResponse;
import com.example.carrentingapp.email.notifications.forgot_password.ForgotPasswordRequest;
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

import java.net.URI;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ForgotPasswordTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private ForgotPasswordVerificationTokenRepository forgotPasswordVerificationTokenRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void correctForgotPasswordTest() throws Exception{
        final String sendEmailURL = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/email";
        final String setNewPasswordURI = "http://localhost:" + randomServerPort + "/api/v1/auth/forgot-password/set-new";

        //wysłanie maila z kodem weryfikacyjnym

        URI sendEmailUri = new URI(sendEmailURL);

        ForgotPasswordVerificationRequest forgotPasswordVerificationRequest = new ForgotPasswordVerificationRequest("adam@kowalski.pl");

        HttpHeaders forgotPasswordVerificationHeaders = new HttpHeaders();
        forgotPasswordVerificationHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ForgotPasswordVerificationRequest> forgotPasswordVerificationRequestHttpEntity
                = new HttpEntity<>(forgotPasswordVerificationRequest, forgotPasswordVerificationHeaders);

        ResponseEntity<ForgotPasswordResponse> forgotPasswordResponseResponse = testRestTemplate.postForEntity(
                sendEmailURL,
                forgotPasswordVerificationRequestHttpEntity,
                ForgotPasswordResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), forgotPasswordResponseResponse.getStatusCode());
        Assertions.assertNotNull(forgotPasswordResponseResponse.getBody());
        Assertions.assertEquals(
                "An email sent, check your inbox and follow the instructions given in message",
                forgotPasswordResponseResponse.getBody().getMessage()
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
        ResponseEntity<ForgotPasswordResponse> forgotPasswordResponseResponseEntity = testRestTemplate

    }
}
