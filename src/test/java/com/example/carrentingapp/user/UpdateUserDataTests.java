package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.user.request.UserDataUpdateRequest;
import com.example.carrentingapp.user.response.UserDataUpdateResponse;
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
public class UpdateUserDataTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private UserBaseRepository userBaseRepository;

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
    public void testCorrectUpdateOwnUserData(){
        final String token = getToken("jan@nowak.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("jan@nowak.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(userBase.getId().toString()),
                Optional.of("Adam"),
                Optional.ofNullable(userBase.getLastName()),
                Optional.of("adam@nowak.pl"),
                Optional.ofNullable(userBase.getDateOfBirth())
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), userDataUpdateResponse.getStatusCode());

        UserBase userAfterUpdate = userBaseRepository.findByEmail("adam@nowak.pl").orElseThrow();

        Assertions.assertEquals(userBase.getId(), userAfterUpdate.getId());
        Assertions.assertNotEquals(userBase.getFirstName(), userAfterUpdate.getFirstName());
        Assertions.assertNotEquals(userBase.getEmail(), userAfterUpdate.getEmail());
    }

    @Test
    public void testCorrectUpdateOtherUserDataAsAdmin(){
        final String token = getToken("adam@kowalski.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("jan@nowak.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(userBase.getId().toString()),
                Optional.of("Adam"),
                Optional.ofNullable(userBase.getLastName()),
                Optional.of("adam@nowak.pl"),
                Optional.ofNullable(userBase.getDateOfBirth())
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), userDataUpdateResponse.getStatusCode());

        UserBase userAfterUpdate = userBaseRepository.findByEmail("adam@nowak.pl").orElseThrow();

        Assertions.assertEquals(userBase.getId(), userAfterUpdate.getId());
        Assertions.assertNotEquals(userBase.getFirstName(), userAfterUpdate.getFirstName());
        Assertions.assertNotEquals(userBase.getEmail(), userAfterUpdate.getEmail());
    }

    @Test
    public void testUpdateNotOwnData(){
        final String token = getToken("jan@nowak.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("adam@kowalski.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(userBase.getId().toString()),
                Optional.of("Adam"),
                Optional.ofNullable(userBase.getLastName()),
                Optional.of("adam@nowak.pl"),
                Optional.ofNullable(userBase.getDateOfBirth())
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), userDataUpdateResponse.getStatusCode());
    }

    @Test
    public void testUpdateUserDataWithNullValues(){
        final String token = getToken("jan@nowak.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("jan@nowak.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(userBase.getId().toString()),
                null,
                null,
                null,
                null
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), userDataUpdateResponse.getStatusCode());
    }

    @Test
    public void testUpdateUserDataWithEmptyValues(){
        final String token = getToken("jan@nowak.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("jan@nowak.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(userBase.getId().toString()),
                null,
                null,
                null,
                null
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), userDataUpdateResponse.getStatusCode());
    }

    @Test
    public void testUpdateDataOfNotExistingUser(){
        final String token = getToken("adam@kowalski.pl");
        final String updateUserDataUrl = "http://localhost:" + randomServerPort + "/api/v1/user/update";
        final UserBase userBase = userBaseRepository.findByEmail("jan@nowak.pl").orElseThrow();

        UserDataUpdateRequest userDataUpdateRequest = new UserDataUpdateRequest(
                Optional.ofNullable(UUID.randomUUID().toString()),
                Optional.of("Adam"),
                Optional.ofNullable(userBase.getLastName()),
                Optional.of("adam@nowak.pl"),
                Optional.ofNullable(userBase.getDateOfBirth())
        );

        HttpHeaders updateUserDataHeaders = new HttpHeaders();
        updateUserDataHeaders.set("X-COM-PERSIST", "true");
        updateUserDataHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UserDataUpdateRequest> userDataUpdateHttpEntity = new HttpEntity<>(userDataUpdateRequest, updateUserDataHeaders);

        ResponseEntity<UserDataUpdateResponse> userDataUpdateResponse = testRestTemplate.postForEntity(
                updateUserDataUrl,
                userDataUpdateHttpEntity,
                UserDataUpdateResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(404), userDataUpdateResponse.getStatusCode());
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
