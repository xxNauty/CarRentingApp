package com.example.carrentingapp.user;

import com.example.carrentingapp.authentication.request.LoginRequest;
import com.example.carrentingapp.authentication.response.AuthenticationResponse;
import com.example.carrentingapp.exception.exceptions.http_error_404.UserLockNotFoundException;
import com.example.carrentingapp.exception.exceptions.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.request.LockRequest;
import com.example.carrentingapp.user.request.UnlockRequest;
import com.example.carrentingapp.user.response.LockResponse;
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
public class UserLockTests {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private UserLockRepository userLockRepository;

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

    //BLOKOWANIE

    @Test
    public void testCorrectUserLock() {
        final String lockURL = "http://localhost:" + randomServerPort + "/api/v1/user/lock";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //logowanie jako admin by uzyskać token weryfikacyjny

        String adminToken = getToken("adam@kowalski.pl");

        //pobranie z bazy id usera do zablokowania

        UUID id = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        //blokowanie użytkownika

        LockRequest lockRequest = new LockRequest(
                Optional.of(id.toString()),
                Optional.of(UserLock.LockType.FOREVER.name()),
                Optional.of(UserLock.Reason.DAMAGED_CAR.name()),
                Optional.empty()
        );

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<LockRequest> lockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockURL,
                lockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), lockResponse.getStatusCode());
        Assertions.assertNotNull(lockResponse.getBody());
        Assertions.assertEquals("User account is now locked", lockResponse.getBody().getMessage());

        //sprawdzenie czy użytkownik faktycznie został zablokowany

        UserBase userAfterLock = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(UserBase.UserStatus.USER_LOCKED_FOREVER, userAfterLock.getStatus());

        UserLock lock = userLockRepository.findActiveForUser(
                userAfterLock.getId()
        ).orElseThrow(() -> new UserLockNotFoundException("User lock not found"));

        Assertions.assertDoesNotThrow(() -> new UserLockNotFoundException("User lock not found"));

        Assertions.assertEquals(UserLock.LockType.FOREVER, lock.getType());
        Assertions.assertNull(lock.getExpirationDate());

        //sprawdzenie czy użytkownik może zalogować się na swoje konto po blokadzie

        LoginRequest lockedUserLoginRequest = new LoginRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Qwerty123!")
        );

        HttpHeaders lockedUserLoginHeaders = new HttpHeaders();
        lockedUserLoginHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> lockedUserLoginHttpEntity = new HttpEntity<>(lockedUserLoginRequest, lockedUserLoginHeaders);

        ResponseEntity<AuthenticationResponse> lockedUserAuthenticationResponse = testRestTemplate.postForEntity(
                loginURL,
                lockedUserLoginHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), lockedUserAuthenticationResponse.getStatusCode());
    }

    @Test
    public void testLockUserAsNotPrivileged() {
        final String lockURL = "http://localhost:" + randomServerPort + "/api/v1/user/lock";

        //logowanie jako user

        String userToken = getToken("jan@nowak.pl");

        //pobranie z bazy id usera do zablokowania

        UUID id = baseUserRepository.findByEmail("adam@kowalski.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        //nieudane blokowanie użytkownika

        LockRequest lockRequest = new LockRequest(
                Optional.of(id.toString()),
                Optional.of(UserLock.LockType.FOREVER.name()),
                Optional.of(UserLock.Reason.DAMAGED_CAR.name()),
                Optional.empty()
        );

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + userToken);

        HttpEntity<LockRequest> lockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockURL,
                lockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), lockResponse.getStatusCode());

        //sprawdzenie czy użytkownik nie został zablokowany

        UserBase userAfterLock = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        Assertions.assertEquals(UserBase.UserStatus.USER_READY, userAfterLock.getStatus());

        UserLock lock = userLockRepository.findActiveForUser(
                userAfterLock.getId()
        ).orElse(new UserLock());

        Assertions.assertEquals(new UserLock(), lock);
    }

    @Test
    public void testUserLockWithNullValues() {
        final String lockURL = "http://localhost:" + randomServerPort + "/api/v1/user/lock";

        //logowanie jako admin by uzyskać token weryfikacyjny

        String adminToken = getToken("adam@kowalski.pl");

        //pobranie z bazy id usera do zablokowania

        UUID id = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        //blokowanie użytkownika

        LockRequest lockRequest = new LockRequest(
                null,
                null,
                null,
                null
        );

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<LockRequest> lockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockURL,
                lockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), lockResponse.getStatusCode());
    }

    @Test
    public void testUserLockWithEmptyValues() {
        final String lockURL = "http://localhost:" + randomServerPort + "/api/v1/user/lock";

        //logowanie jako admin by uzyskać token weryfikacyjny

        String adminToken = getToken("adam@kowalski.pl");

        //pobranie z bazy id usera do zablokowania

        UUID id = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        //blokowanie użytkownika

        LockRequest lockRequest = new LockRequest(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.empty()
        );

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<LockRequest> lockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockURL,
                lockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), lockResponse.getStatusCode());
    }

    //ODBLOKOWYWANIE

    @Test
    public void testCorrectUserUnlock() {
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //blokowanie użytkownika

        UUID userID = lockUser("jan@nowak.pl", false);

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("adam@kowalski.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(Optional.of(userID.toString()));

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), lockResponse.getStatusCode());
        Assertions.assertNotNull(lockResponse.getBody());
        Assertions.assertEquals("User account is now unlocked", lockResponse.getBody().getMessage());

        //sprawdzenie czy użytkownik może się spowrotem logować

        LoginRequest loginRequest = new LoginRequest(
                Optional.of("jan@nowak.pl"),
                Optional.of("Qwerty123!")
        );

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> loginHttpEntity = new HttpEntity<>(loginRequest, loginHeaders);

        ResponseEntity<AuthenticationResponse> authenticationResponse = testRestTemplate.postForEntity(
                loginURL,
                loginHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(200), authenticationResponse.getStatusCode());
    }

    @Test
    public void testUnlockUserAsNotPrivilegedUser() {
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //blokowanie użytkownika

        UUID userID = lockUser("adam@kowalski.pl", false);

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("jan@nowak.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(Optional.ofNullable(userID.toString()));

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), lockResponse.getStatusCode());

        //sprawdzenie czy użytkownik napewno nie został odblokowany

        LoginRequest loginRequest = new LoginRequest(
                Optional.of("adam@kowalski.pl"),
                Optional.of("Qwerty123!")
        );

        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.set("X-COM-PERSIST", "true");

        HttpEntity<LoginRequest> loginHttpEntity = new HttpEntity<>(loginRequest, loginHeaders);

        ResponseEntity<AuthenticationResponse> authenticationResponse = testRestTemplate.postForEntity(
                loginURL,
                loginHttpEntity,
                AuthenticationResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(403), authenticationResponse.getStatusCode());
    }

    @Test
    public void testUnlockNotLockedUser() {
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";

        //pobranie Id użytkownika, bez blokowania jego konta

        UUID userID = baseUserRepository.findByEmail("jan@nowak.pl").orElseThrow(() -> new UserNotFoundException("User not found")).getId();

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("adam@kowalski.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(Optional.ofNullable(userID.toString()));

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(409), lockResponse.getStatusCode());
    }

    @Test
    public void testUserUnlockWithNullValues() {
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";
        final String loginURL = "http://localhost:" + randomServerPort + "/api/v1/auth/login";

        //blokowanie użytkownika

        UUID userID = lockUser("jan@nowak.pl", false);

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("adam@kowalski.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(null);

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), lockResponse.getStatusCode());
    }

    @Test
    public void testUserUnlockWithEmptyValues() {
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";

        //blokowanie użytkownika

        UUID userID = lockUser("jan@nowak.pl", false);

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("adam@kowalski.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(Optional.empty());

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(500), lockResponse.getStatusCode());
    }

    @Test
    public void testUnlockLockedForeverUser(){
        final String unlockURL = "http://localhost:" + randomServerPort + "/api/v1/user/unlock";

        //blokowanie użytkownika

        UUID userID = lockUser("jan@nowak.pl", true);

        //pobranie tokenu weryfikacyjnego konta uprawnionego do odblokowania

        String token = getToken("adam@kowalski.pl");

        //odblokowywanie

        UnlockRequest unlockRequest = new UnlockRequest(Optional.ofNullable(userID.toString()));

        HttpHeaders unlockHeaders = new HttpHeaders();
        unlockHeaders.set("X-COM-PERSIST", "true");
        unlockHeaders.set("Authorization", "Bearer " + token);

        HttpEntity<UnlockRequest> unlockRequestHttpEntity = new HttpEntity<>(unlockRequest, unlockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                unlockURL,
                unlockRequestHttpEntity,
                LockResponse.class
        );

        Assertions.assertEquals(HttpStatusCode.valueOf(409), lockResponse.getStatusCode());
    }

    //==================================================================================================================

    private UUID lockUser(String userEmail, boolean isForever) {
        final String lockURL = "http://localhost:" + randomServerPort + "/api/v1/user/lock";

        String adminToken = getToken("adam@kowalski.pl");

        UUID id = baseUserRepository.findByEmail(userEmail).orElseThrow(() -> new UserNotFoundException("User not found")).getId();
        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));

        LockRequest lockRequest;

        if(isForever){
            lockRequest = new LockRequest(
                    Optional.of(id.toString()),
                    Optional.of(UserLock.LockType.FOREVER.name()),
                    Optional.of(UserLock.Reason.DAMAGED_CAR.name()),
                    Optional.empty()
            );
        }
        else {
            lockRequest = new LockRequest(
                    Optional.of(id.toString()),
                    Optional.of(UserLock.LockType.TEMPORARY.name()),
                    Optional.of(UserLock.Reason.DAMAGED_CAR.name()),
                    Optional.of(LocalDate.now().plusMonths(1))
            );
        }

        HttpHeaders lockHeaders = new HttpHeaders();
        lockHeaders.set("X-COM-PERSIST", "true");
        lockHeaders.set("Authorization", "Bearer " + adminToken);

        HttpEntity<LockRequest> lockRequestHttpEntity = new HttpEntity<>(lockRequest, lockHeaders);

        ResponseEntity<LockResponse> lockResponse = testRestTemplate.postForEntity(
                lockURL,
                lockRequestHttpEntity,
                LockResponse.class
        );

        return id;
    }

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
