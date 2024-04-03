package com.example.carrentingapp.other;


import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.email.contact_form.request.ContactFormRequest;
import com.example.carrentingapp.email.contact_form.response.ContactFormResponse;
import com.example.carrentingapp.email.message_history.EmailMessageRepository;
import com.example.carrentingapp.user.UserBase;
import com.example.carrentingapp.user.UserBaseRepository;
import org.checkerframework.checker.units.qual.A;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ContactFormTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailMessageRepository emailMessageRepository;

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
        userBaseRepository.save(admin);

        UserBase user = new UserBase(
                "Jan",
                "Nowak",
                "jan@nowak.pl",
                passwordEncoder.encode("Qwerty123!"),
                LocalDate.now().minusYears(18)
        );
        user.setStatus(UserBase.UserStatus.USER_READY);
        userBaseRepository.save(user);
    }

    @Test
    public void testNotAuthorizedContactFormAsNotAuthorized(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/no_auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();

        ContactFormRequest request = new ContactFormRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Temat"),
                Optional.of("Lorem ipsum dolor sit amet")
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore + 1, emailMessagesCountAfter);
    }

    @Test
    public void testNotAuthorizedContactFormAsAuthorized(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/no_auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                Optional.of("jan@kowalski.pl"),
                Optional.of("Temat"),
                Optional.of("Lorem ipsum dolor sit amet")
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore + 1, emailMessagesCountAfter);
    }

    @Test
    public void testAuthorizedContactFormAsNotAuthorized(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();

        ContactFormRequest request = new ContactFormRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Temat"),
                Optional.of("Lorem ipsum dolor sit amet")
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(403), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore, emailMessagesCountAfter);
    }

    @Test
    public void testAuthorizedContactFormAsAuthorized(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Temat"),
                Optional.of("Lorem ipsum dolor sit amet")
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(200), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore + 1, emailMessagesCountAfter);
    }

    @Test
    public void testAuthorizedContactFormWithNullValues(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                null,
                null,
                null
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore, emailMessagesCountAfter);
    }

    @Test
    public void testUnauthorizedContactFormWithEmptyValues(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/no_auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore, emailMessagesCountAfter);
    }

    @Test
    public void testUnauthorizedContactFormWithNullValues(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/no_auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                null,
                null,
                null
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore, emailMessagesCountAfter);
    }

    @Test
    public void testAuthorizedContactFormWithEmptyValues(){
        final String contactFormUrl = "http://localhost:" + randomServerPort + "/api/v1/contact_form/auth";
        final int emailMessagesCountBefore = emailMessageRepository.findAll().size();
        final String token = getToken("jan@nowak.pl");

        ContactFormRequest request = new ContactFormRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders contactFormHeaders = new HttpHeaders();
        contactFormHeaders.set("X-COM-PERSIST", "true");
        contactFormHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<ContactFormRequest> contactFormRequestHttpEntity = new HttpEntity<>(request, contactFormHeaders);

        ResponseEntity<ContactFormResponse> contactFormResponse = testRestTemplate.postForEntity(
                contactFormUrl,
                contactFormRequestHttpEntity,
                ContactFormResponse.class
        );

        final int emailMessagesCountAfter = emailMessageRepository.findAll().size();

        Assertions.assertEquals(HttpStatusCode.valueOf(500), contactFormResponse.getStatusCode());
        Assertions.assertEquals(emailMessagesCountBefore, emailMessagesCountAfter);
    }

    //==================================================================================================================

    private String getToken(String email) {
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        LoginRequest loginRequest = new LoginRequest(
                Optional.of(email),
                Optional.of("Qwerty123!")
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
