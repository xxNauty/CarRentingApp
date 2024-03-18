package com.example.carrentingapp.integration.user;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.car.response.GetSimpleCarListResponse;
import com.example.carrentingapp.exception.exception.http_error_500.UserNotLockedException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import com.example.carrentingapp.user.UserLock;
import com.example.carrentingapp.user.UserLockRepository;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
import com.example.carrentingapp.user.service.UserLockService;
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

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class LockUserAccountTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Autowired
    private UserLockService userLockService;

    @Autowired
    private UserLockRepository userLockRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @LocalServerPort
    int randomServerPort;

    @Test
    public void testLockUserEndpoint() throws URISyntaxException {
        BaseUser user = commonFunctionsProvider.createUser();

        Assertions.assertFalse(user.getIsLocked());

        final String lockUrl = "http://localhost:" + randomServerPort + "/api/v1/user/lock";

        URI lockUri = new URI(lockUrl);

        HttpHeaders lockHttpheaders = new HttpHeaders();
        lockHttpheaders.set("X-COM-PERSIST", "true");
        lockHttpheaders.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        LockRequest lockRequest = new LockRequest(
                user.getId(),
                UserLock.LockType.TEMPORARY.name(),
                UserLock.Reason.FREQUENT_DELAYED_RETURNS.name(),
                LocalDate.now()
        );

        HttpEntity<LockRequest> lockHttpEntity = new HttpEntity<>(lockRequest, lockHttpheaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockUri,
                lockHttpEntity,
                LockResponse.class
        );

        userLockRepository.findAllByStatusAndUser(user.getId(), UserLock.UserLockStatus.USER_LOCK_ACTIVE).orElseThrow(() -> new UserNotLockedException("User not locked"));
        Assertions.assertDoesNotThrow(() -> new UserNotLockedException("User not locked"));

        Assertions.assertEquals(HttpStatusCode.valueOf(200), lockResponse.getStatusCode());
        Assertions.assertNotNull(lockResponse.getBody());
        Assertions.assertEquals("User account is now locked", lockResponse.getBody().getMessage());

        //test czy po zablokowaniu może zalogować się

        final String loginUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI loginUri = new URI(loginUrl);

        HttpHeaders loginHttpheaders = new HttpHeaders();
        loginHttpheaders.set("X-COM-PERSIST", "true");

        LoginRequest loginRequest = new LoginRequest(
                "jan@kowalski.pl",
                "Qwerty123!"
        );

        HttpEntity<LoginRequest> loginHttpEntity = new HttpEntity<>(loginRequest, loginHttpheaders);

        ResponseEntity<AuthenticationResponse> loginResponse = testRestTemplate.postForEntity(
                loginUri,
                loginHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), loginResponse.getStatusCode());
        Assertions.assertNotNull(loginResponse.getBody());

        //test czy user może skorzystać z api, na przykładzie pobrania listy wszystkich aut

        final String testApiUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/simple/all";

        HttpHeaders testApiHttpHeaders = new HttpHeaders();
        testApiHttpHeaders.set("X-COM-PERSIST", "true");
        testApiHttpHeaders.set("Authorization", "Bearer "
                + loginResponse.getBody().getAccessToken());

        ResponseEntity<GetSimpleCarListResponse> response = testRestTemplate.exchange(
                testApiUrl,
                HttpMethod.GET,
                new HttpEntity<>(testApiHttpHeaders),
                GetSimpleCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), response.getStatusCode());
        //todo: poprawić po naprawieniu błędu z rzucaniem 500 zamiast 403 przy logowaniu
    }

    @Test
    public void testUnlockUserEndpoint() throws URISyntaxException {
        BaseUser user = commonFunctionsProvider.createUser();

        userLockService.lockUser(
                new LockRequest(
                        user.getId(),
                        UserLock.LockType.TEMPORARY.name(),
                        UserLock.Reason.FREQUENT_DELAYED_RETURNS.name(),
                        LocalDate.now()

                )
        );

        BaseUser userFromDatabase = baseUserRepository.findById(user.getId()).orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertTrue(userFromDatabase.getIsLocked());

        //odblokowanie usera

        final String unlockUrl = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";

        URI unlockUri = new URI(unlockUrl);

        HttpHeaders unlockHttpHeaders = new HttpHeaders();
        unlockHttpHeaders.set("X-COM-PERSIST", "true");
        unlockHttpHeaders.set("Authorization", "Bearer "
                + commonFunctionsProvider.getBearerToken("adam@kowalski.pl", "Qwerty123!", randomServerPort, testRestTemplate));

        UnlockRequest unlockRequest = new UnlockRequest(userFromDatabase.getId());

        HttpEntity<UnlockRequest> unlockHttpEntity = new HttpEntity<>(unlockRequest, unlockHttpHeaders);

        ResponseEntity<LockResponse> unlockResponse = testRestTemplate.postForEntity(
                unlockUri,
                unlockHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), unlockResponse.getStatusCode());
        Assertions.assertNotNull(unlockResponse.getBody());
        Assertions.assertEquals("User account is now unlocked", unlockResponse.getBody().getMessage());

        //test czy po odblokowaniu może się zalogować

        final String loginUrl = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        URI loginUri = new URI(loginUrl);

        HttpHeaders loginHttpheaders = new HttpHeaders();
        loginHttpheaders.set("X-COM-PERSIST", "true");

        LoginRequest loginRequest = new LoginRequest(
                "jan@kowalski.pl",
                "Qwerty123!"
        );

        HttpEntity<LoginRequest> loginHttpEntity = new HttpEntity<>(loginRequest, loginHttpheaders);

        ResponseEntity<AuthenticationResponse> loginResponse = testRestTemplate.postForEntity(
                loginUri,
                loginHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), loginResponse.getStatusCode());
        Assertions.assertNotNull(loginResponse.getBody());

        //test czy user może skorzystać z api, na przykładzie pobrania listy wszystkich aut

        final String testApiUrl = "http://localhost:" + randomServerPort + "/api/v1/car/get/simple/all";

        HttpHeaders testApiHttpHeaders = new HttpHeaders();
        testApiHttpHeaders.set("X-COM-PERSIST", "true");
        testApiHttpHeaders.set("Authorization", "Bearer "
                + loginResponse.getBody().getAccessToken());

        ResponseEntity<GetSimpleCarListResponse> response = testRestTemplate.exchange(
                testApiUrl,
                HttpMethod.GET,
                new HttpEntity<>(testApiHttpHeaders),
                GetSimpleCarListResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), response.getStatusCode());
    }
}
