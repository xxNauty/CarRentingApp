package com.example.carrentingapp.unit.email;

import com.example.carrentingapp.CommonFunctionsProvider;
import com.example.carrentingapp.authentication.service.AuthenticationService;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationToken;
import com.example.carrentingapp.email.notifications.confirm_email.token.ConfirmationTokenRepository;
import com.example.carrentingapp.exception.exception.http_error_404.TokenNotFoundException;
import com.example.carrentingapp.exception.exception.http_error_404.UserNotFoundException;
import com.example.carrentingapp.user.BaseUser;
import com.example.carrentingapp.user.BaseUserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ConfirmationTokenTest {

    @Autowired
    private ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    private BaseUserRepository baseUserRepository;

    @Autowired
    private CommonFunctionsProvider commonFunctionsProvider;

    @Autowired
    private AuthenticationService authenticationService;

    @Test
    public void testCreateConfirmationToken(){
        BaseUser user = commonFunctionsProvider.createUser();
        ConfirmationToken token = confirmationTokenRepository.findByUser(user).orElseThrow(()
                -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));


        Assertions.assertFalse(user.getIsEnabled());
        Assertions.assertNull(token.getConfirmedAt());

        authenticationService.verifyEmail(token.getToken());

        BaseUser userFromDatabase = baseUserRepository.findById(user.getId()).orElseThrow(()
                -> new UserNotFoundException("User not found"));
        ConfirmationToken tokenFromDatabase = confirmationTokenRepository.findById(token.getId()).orElseThrow(()
                -> new TokenNotFoundException("Token not found"));

        Assertions.assertDoesNotThrow(() -> new UserNotFoundException("User not found"));
        Assertions.assertDoesNotThrow(() -> new TokenNotFoundException("Token not found"));

        Assertions.assertTrue(userFromDatabase.getIsEnabled());
        Assertions.assertNotNull(tokenFromDatabase.getConfirmedAt());
    }

    //todo: testy integracyjne

}
